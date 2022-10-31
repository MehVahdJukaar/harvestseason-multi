package net.mehvahdjukaar.harvestseason.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class HarvestSeasonFabric implements ModInitializer {


    @Override
    public void onInitialize() {

        HarvestSeason.commonInit();

        FabricSetupCallbacks.COMMON_SETUP.add(HarvestSeasonFabric::commonSetup);


        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(HarvestSeasonFabric::initClient);
        }

        UseBlockCallback.EVENT.register(HarvestSeason::onRightClickBlock);
    }

    private static void initClient() {
        ClientRegistry.init();
    }

    private static void commonSetup() {
        HarvestSeason.commonSetup();
    }


}
