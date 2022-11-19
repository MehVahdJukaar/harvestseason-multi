package net.mehvahdjukaar.harvestseason;

import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlock;
import net.mehvahdjukaar.harvestseason.integration.FDCompat;
import net.mehvahdjukaar.harvestseason.network.NetworkHandler;
import net.mehvahdjukaar.harvestseason.configs.ModConfigs;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: MehVahdJukaar
 */
public class HarvestSeason {

    public static final String MOD_ID = "harvestseason";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean SUPP_INSTALLED = PlatformHelper.isModLoaded("supplementaries");
    public static final boolean FD_INSTALLED = PlatformHelper.isModLoaded("farmersdelight");

    public static void commonInit() {
        ModConfigs.earlyLoad();

        ModRegistry.init();
        if (FD_INSTALLED) FDCompat.init();
        NetworkHandler.registerMessages();
    }

    //needs to be fired after configs are loaded
    public static void commonSetup() {
        ComposterBlock.COMPOSTABLES.put(ModRegistry.MOD_CARVED_PUMPKIN.get().asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModRegistry.CORN_SEEDS.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModRegistry.COB_ITEM.get().asItem(), 0.5F);
    }


    public static void onConfigReload() {
    }

    public static final TagKey<Item> MODDED_CANDIES = itemTag("candy");
    public static final TagKey<Item> CARVERS = itemTag("pumpkin_carvers");
    public static final TagKey<Item> CARVABLE_PUMPKINS = itemTag("carvable_pumpkins");

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, HarvestSeason.res(name));
    }

    @EventCalled
    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Direction direction = hit.getDirection();
        if (direction == Direction.UP && HSPlatformStuff.isTopCarver(stack)) {
            BlockPos pos = hit.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.PUMPKIN)) {
                if (!level.isClientSide) {

                    level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemEntity itemEntity = new ItemEntity(level,
                            pos.getX() + 0.5, pos.getY() + 1.15f, pos.getZ() + 0.5,
                            new ItemStack(Items.PUMPKIN_SEEDS, 4));

                    itemEntity.setDeltaMovement(level.random.nextDouble() * 0.02, 0.05 + level.random.nextDouble() * 0.02, level.random.nextDouble() * 0.02);
                    level.addFreshEntity(itemEntity);

                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                    stack.hurtAndBreak(1, player, (l) -> l.broadcastBreakEvent(hand));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    level.gameEvent(player, GameEvent.SHEAR, pos);
                    level.setBlock(pos, ModRegistry.MOD_CARVED_PUMPKIN.get().withPropertiesOf(state)
                            .setValue(ModCarvedPumpkinBlock.FACING, player.getDirection().getOpposite()), 11);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }


}
