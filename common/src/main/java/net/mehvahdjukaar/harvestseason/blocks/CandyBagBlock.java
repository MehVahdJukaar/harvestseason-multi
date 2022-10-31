package net.mehvahdjukaar.harvestseason.blocks;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

import static net.mehvahdjukaar.harvestseason.blocks.PaperBagBlock.SHAPE;

public class CandyBagBlock extends Block implements EntityBlock {

    public static final EnumProperty<Content> CONTENT = EnumProperty.create("content", Content.class);
    public static final IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 1, 6);

    public CandyBagBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CONTENT, Content.POPCORN).setValue(FILL_LEVEL, 1));
    }

    public static boolean tryFilling(Player player, Level level, BlockPos pos, ItemStack stack) {
        Content content = Content.get(stack);
        if (content != null) {
            ItemStack remove = player.isCreative() ? stack.copy() : stack.split(1);
            remove.setCount(1);
            level.setBlockAndUpdate(pos, ModRegistry.CANDY_BAG.get().defaultBlockState().setValue(CONTENT, content));
            playSound(level, pos);
            if (content == Content.OTHER_CANDY) {
                if (level.getBlockEntity(pos) instanceof CandyBagTile tile) {
                    tile.setDisplayedItem(remove);
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTENT, FILL_LEVEL);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = super.getDrops(state, builder);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof CandyBagTile tile) {


        }
        var i = getContent(state);
        if (i != null) {
            list.add(new ItemStack(i, state.getValue(FILL_LEVEL)));
        }
        return list;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {

        Item item;
        if (level.getBlockEntity(pos) instanceof CandyBagTile tile) {
            item = null;
        } else {
            item = getContent(state);
        }
        if (item == null) return InteractionResult.PASS;

        int fill = state.getValue(FILL_LEVEL);
        int delta = 0;
        ItemStack held = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && held.isEmpty()) {
            ItemStack extracted = new ItemStack(item);
            if (!extracted.isEmpty()) {
                Utils.swapItem(player, hand, extracted);
                delta = -1;
            }
        } else if (fill != 6 && Content.get(held) == state.getValue(CONTENT)) {
            if (!player.isCreative()) held.shrink(1);
            playSound(level, pos);
            delta += 1;
        } else {
            if (item.isEdible() && player.canEat(false) && !player.isCreative()) {
                //eat cookies
                player.eat(level, new ItemStack(item));
                delta = -1;
                if (level.isClientSide) {
                    ParticleOptions particleOptions = new ItemParticleOption(ParticleTypes.ITEM, item.getDefaultInstance());
                    double dy = 0.005 + fill / 16d;
                    double power = 0.2;
                    for (int i = 0; i < 12; ++i) {
                        level.addParticle(particleOptions,
                                pos.getX() + 2 / 16f + level.random.nextFloat() * 12 / 16f,
                                pos.getY() + dy,
                                pos.getZ() + 2 / 16f + level.random.nextFloat() * 12 / 16f,
                                (level.random.nextFloat() - 0.5) * power,
                                (level.random.nextFloat() - 0.5) * power,
                                (level.random.nextFloat() - 0.5) * power);
                    }
                }
            }
        }
        if (delta != 0) {

            int newFill = fill + delta;
            if (newFill == 0) {
                level.setBlockAndUpdate(pos, ModRegistry.PAPER_BAG.get().defaultBlockState());
            } else {
                level.setBlockAndUpdate(pos, state.setValue(FILL_LEVEL, newFill));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private static void playSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.PLAYERS, 1, 1.2f);
    }

    @Nullable
    public Item getContent(BlockState state) {
        var c = state.getValue(CONTENT);
        if (c.drop != null) {
            var i = Registry.ITEM.getOptional(new ResourceLocation(c.drop));
            if (i.isPresent()) return i.get();
        }
        return null;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(CONTENT) == Content.OTHER_CANDY ? new CandyBagTile(pos, state) : null;
    }

    public enum Content implements StringRepresentable {
        CANDY("supplementaries:candy"),
        CANDY_CANE("snowyspirit:candy_cane"),
        CANDY_CORN("harvestseason:candy_corn"),
        POPCORN("harvestseason:popcorn"),
        KERNELS("harvestseason:kernels"),
        OTHER_CANDY(null);

        private final String drop;

        Content(String drop) {
            this.drop = drop;
        }

        @Nullable
        public static Content get(ItemStack item) {
            if (item.isEmpty()) return null;
            String name = Utils.getID(item.getItem()).toString();
            for (var c : Content.values()) {
                if (c.drop != null && c.drop.equals(name)) return c;
            }
            if (item.is(HarvestSeason.MODDED_CANDIES)) return OTHER_CANDY;
            return null;
        }

        @Override
        public String getSerializedName() {
            return this.toString().toLowerCase(Locale.ROOT);
        }
    }
}
