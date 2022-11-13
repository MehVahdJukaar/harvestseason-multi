package net.mehvahdjukaar.harvestseason.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HarvestSeason.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HarvestSeasonForgeClient {


    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(ClientRegistry::setup);
    }

    @SubscribeEvent
    public static void registerGuiLayer(RegisterGuiOverlaysEvent event) {
        // event.registerBelow(VanillaGuiOverlay.HELMET.id(), "harvestseason",
        //         new PumpkinBlurGuiOverlay(Minecraft.getInstance()));
    }

    private static ShaderInstance blur;

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) {
        try {
            var blur = new ShaderInstance(event.getResourceManager(), HarvestSeason.res("blur"),
                    DefaultVertexFormat.POSITION_TEX);

            event.registerShader(blur, s -> HarvestSeasonForgeClient.blur = s);
        } catch (Exception e) {
            HarvestSeason.LOGGER.error("Failed to parse blur shader");
        }
    }

    public static ShaderInstance getBlur(){
       // blur.getUniform("Radius").set(8f);
        return blur;
    }
}
