package net.mehvahdjukaar.harvestseason.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;

public class HarvestSeasonFabric implements ModInitializer {


    @Override
    public void onInitialize() {

        HarvestSeason.commonInit();

        FabricSetupCallbacks.COMMON_SETUP.add(HarvestSeasonFabric::commonSetup);


        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(HarvestSeasonFabric::initClient);
        }
    }

    private static void initClient() {
        ClientRegistry.init();
    }

    private static void commonSetup() {
        HarvestSeason.commonSetup();
    }


}
