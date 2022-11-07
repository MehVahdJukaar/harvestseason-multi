package net.mehvahdjukaar.harvestseason.blocks;

import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.IBeeGrowable;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornBaseBlock extends CropBlock implements IBeeGrowable {
    public CornBaseBlock(Properties properties) {
        super(properties);
    }

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6, 0.0, 6, 10.0, 4.0, 10.0),
            Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)};


    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (getAge(state) == getMaxAge()) {
            if (!level.getBlockState(pos.above()).is(ModRegistry.CORN_MIDDLE.get())) return false;
        }
        return super.canSurvive(state, level, pos);
    }

    public boolean canGrowUp(BlockGetter worldIn, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState state = worldIn.getBlockState(above);
        return state.getBlock() instanceof CornMiddleBlock cb && cb.canGrowUp(worldIn, above) || state.getMaterial().isReplaceable();
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return this.getAge(state) + 1 < this.getMaxAge() || this.canGrowUp(worldIn, pos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!PlatformHelper.isAreaLoaded(level, pos, 1))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9) {
            if (this.isValidBonemealTarget(level, pos, state, level.isClientSide)) {
                float f = getGrowthSpeed(this, level, pos);
                if (ForgeHelper.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
                    this.growCropBy(level, pos, state, 1);
                    ForgeHelper.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }


    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModRegistry.CORN_SEEDS.get();
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return super.getBonemealAgeIncrease(level) / 3;
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        growCropBy(level, pos, state, this.getBonemealAgeIncrease(level));
    }

    public void growCropBy(Level level, BlockPos pos, BlockState state, int increment) {
        int newAge = this.getAge(state) + increment;
        int maxAge = this.getMaxAge();
        if (newAge > maxAge) {
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.getBlock() instanceof CornMiddleBlock cm) {
                cm.growCropBy(level, above, state, increment);
            }
        } else {
            if (newAge == maxAge) {
                level.setBlock(pos.above(), ModRegistry.CORN_MIDDLE.get().defaultBlockState(), 2);
            }
            level.setBlock(pos, getStateForAge(newAge), 2);
        }
    }

    @Override
    public boolean getPollinated(Level level, BlockPos pos, BlockState state) {
        growCropBy(level, pos, state, 1);
        return true;
    }

}
