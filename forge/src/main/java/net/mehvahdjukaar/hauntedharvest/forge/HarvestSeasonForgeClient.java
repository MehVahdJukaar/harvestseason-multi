package net.mehvahdjukaar.hauntedharvest.forge;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HarvestSeason.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HarvestSeasonForgeClient {


    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.setup();
        });
    }
}
