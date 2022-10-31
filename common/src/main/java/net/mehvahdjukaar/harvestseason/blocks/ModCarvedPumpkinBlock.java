package net.mehvahdjukaar.harvestseason.blocks;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

//TODO: IOwner protected
public class ModCarvedPumpkinBlock extends CarvedPumpkinBlock implements EntityBlock {


    public ModCarvedPumpkinBlock(Properties properties) {
        super(properties);
    }

    public static Pair<Integer, Integer> getHitSubPixel(BlockHitResult hit) {
        Vec3 v2 = hit.getLocation();
        Vec3 v = v2.yRot((float) ((hit.getDirection().toYRot()) * Math.PI / 180f));
        double fx = ((v.x % 1) * 16);
        if (fx < 0) fx += 16;
        int x = Mth.clamp((int) fx, -15, 15);

        int y = 15 - (int) Mth.clamp(Math.abs((v.y % 1) * 16), 0, 15);
        if (v2.y < 0) y = 15 - y; //crappy logic
        return new Pair<>(x, y);
    }

    public static boolean isCarverItem(ItemStack stack) {
        return PlatformHelper.getPlatform().isFabric() ? stack.getItem() instanceof SwordItem : stack.is(HarvestSeason.CARVERS);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof ModCarvedPumpkinBlockTile te &&
                te.isAccessibleBy(player) && !te.isWaxed()) {
            ItemStack stack = player.getItemInHand(handIn);
            Item i = stack.getItem();
            if (i instanceof HoneycombItem) {
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                level.levelEvent(player, 3003, pos, 0);
                te.setWaxed(true);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (!te.isJackOLantern() && i == Items.TORCH) {
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                CompoundTag tag = new CompoundTag();
                te.saveAdditional(tag);
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.PLAYERS, 1, 1.3f);
                level.setBlockAndUpdate(pos, ModRegistry.MOD_JACK_O_LANTERN.get().withPropertiesOf(state));
                if (level.getBlockEntity(pos) instanceof ModCarvedPumpkinBlockTile jack) {
                    jack.load(tag);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            CarveMode mode = te.getCarveMode();

            if (mode != CarveMode.NONE) {
                if (hit.getDirection() == state.getValue(FACING) && mode.canManualDraw() && isCarverItem(stack)) {

                    Pair<Integer, Integer> pair = getHitSubPixel(hit);
                    int x = pair.getFirst();
                    int y = pair.getSecond();

                    te.setPixel(x, y, !te.getPixel(x, y));
                    te.setChanged();
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                if (!level.isClientSide && mode.canOpenGui()) {
                    te.sendOpenGuiPacket(level, pos, player);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public enum CarveMode {
        NONE, BOTH, GUI, MANUAL;

        public boolean canOpenGui() {
            return this != MANUAL && this != NONE;
        }

        public boolean canManualDraw() {
            return this != GUI && this != NONE;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModCarvedPumpkinBlockTile(pPos, pState);
    }

    public ItemStack getBlackboardItem(ModCarvedPumpkinBlockTile te) {
        ItemStack itemstack = new ItemStack(this);
        if (!te.isEmpty()) {
            CompoundTag tag = te.savePixels(new CompoundTag());
            if (!tag.isEmpty()) {
                itemstack.addTagElement("BlockEntityTag", tag);
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof ModCarvedPumpkinBlockTile te) {
            return this.getBlackboardItem(te);
        }
        return super.getCloneItemStack(level, pos, state);
    }
}
