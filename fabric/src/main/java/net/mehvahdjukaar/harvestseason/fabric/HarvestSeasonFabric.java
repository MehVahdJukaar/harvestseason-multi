package net.mehvahdjukaar.harvestseason.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.client.ICustomItemRendererProvider;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.world.level.ItemLike;

public class HarvestSeasonFabric implements ModInitializer {


    @Override
    public void onInitialize() {

        HarvestSeason.commonInit();

        FabricSetupCallbacks.COMMON_SETUP.add(HarvestSeasonFabric::commonSetup);


        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(HarvestSeasonFabric::clientSetup);
        }

        UseBlockCallback.EVENT.register(HarvestSeason::onRightClickBlock);
    }

    private static void clientSetup() {
        ClientRegistry.init();

        registerISTER(ModRegistry.MOD_JACK_O_LANTERN_ITEM.get());
        registerISTER(ModRegistry.MOD_CARVED_PUMPKIN_ITEM.get());

        ClientRegistry.setup();
    }


    private static void registerISTER(ItemLike itemLike) {
        ((ICustomItemRendererProvider) itemLike.asItem()).registerFabricRenderer();
    }


    private static void commonSetup() {
        HarvestSeason.commonSetup();
    }


}
