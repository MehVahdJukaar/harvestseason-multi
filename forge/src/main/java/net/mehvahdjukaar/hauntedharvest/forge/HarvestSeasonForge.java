package net.mehvahdjukaar.hauntedharvest.forge;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Author: MehVahdJukaar
 */
@Mod(HarvestSeason.MOD_ID)
public class HarvestSeasonForge {

    public HarvestSeasonForge() {


        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(HarvestSeasonForge::init);

        HarvestSeason.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            ClientRegistry.init();
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(HarvestSeason::commonSetup);
    }

    @SubscribeEvent
    public static void onTagLoad(TagsUpdatedEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onUseBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled()) {
            var ret = HarvestSeason.onRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            if (ret != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(ret);
            }
        }
    }

}
