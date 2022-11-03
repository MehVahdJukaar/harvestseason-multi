package net.mehvahdjukaar.harvestseason.blocks;

import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornMiddleBlock extends CropBlock {

    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(1, 0.0, 1, 15.0, 5.0, 15.0),
            Block.box(1, 0.0, 1, 15.0, 11.0, 15.0),
            Block.box(1, 0.0, 1, 15.0, 16.0, 15.0)};


    public CornMiddleBlock(Properties properties) {
        super(properties);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (getAge(state) == getMaxAge()) {
            if (!level.getBlockState(pos.above()).is(ModRegistry.CORN_TOP.get())) return false;
        }
        BlockState below = level.getBlockState(pos.below());
        if (!(below.getBlock() instanceof CornBaseBlock base) || !base.isMaxAge(below)) return false;
        return super.canSurvive(state, level, pos);
    }

    // Tick function
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!PlatformHelper.isAreaLoaded(level, pos, 1))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (this.isValidBonemealTarget(level, pos, state, level.isClientSide)) {
                float f = getGrowthSpeed(this, level, pos);
                if (ForgeHelper.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
                    if (age + 1 == this.getMaxAge()) {
                        level.setBlock(pos.above(), ModRegistry.CORN_TOP.get().defaultBlockState(), 3);
                    }
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        InteractionResult old = super.use(state, world, pos, player, hand, rayTraceResult);
        if (!old.consumesAction()) {
            var ev = ForgeHelper.onRightClickBlock(player, hand, pos.below(), rayTraceResult);
            if (ev != null) return ev;
        }
        return old;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModRegistry.CORN_BASE.get());
    }

}
