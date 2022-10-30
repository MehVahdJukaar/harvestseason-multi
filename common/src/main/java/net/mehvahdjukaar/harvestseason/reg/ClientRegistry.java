package net.mehvahdjukaar.harvestseason.reg;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.client.CarvedPumpkinBlockLoader;
import net.mehvahdjukaar.harvestseason.client.CarvedPumpkinTileRenderer;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;

public class ClientRegistry {

    public static final Material CARVED_PUMPKIN_BACKGROUND = new Material(LOCATION_BLOCKS, HarvestSeason.res("block/carved_pumpkin_background"));
    public static final Material CARVED_PUMPKIN_SHADE = new Material(LOCATION_BLOCKS, HarvestSeason.res("block/carved_pumpkin_shade"));

    public static final Material JACK_O_LANTERN_BACKGROUND = new Material(LOCATION_BLOCKS,HarvestSeason.res("block/jack_o_lantern_background"));
    public static final Material JACK_O_LANTERN_SHADE_1 =new Material(LOCATION_BLOCKS, HarvestSeason.res("block/jack_o_lantern_shade_1"));
    public static final Material JACK_O_LANTERN_SHADE_2 =new Material(LOCATION_BLOCKS, HarvestSeason.res("block/jack_o_lantern_shade_2"));

    public static final Material PUMPKIN_HIGHLIGHT = new Material(LOCATION_BLOCKS, HarvestSeason.res("block/pumpkin_highlight"));
    public static final Material PUMPKIN = new Material(LOCATION_BLOCKS, new ResourceLocation("block/pumpkin_side"));

    public static final Material CARVING_OUTLINE = new Material(LOCATION_BLOCKS, HarvestSeason.res("block/carving_grid"));


    public static void init() {
        ClientPlatformHelper.addAtlasTextureCallback(TextureAtlas.LOCATION_BLOCKS, ClientRegistry::registerTextures);
        ClientPlatformHelper.addModelLoaderRegistration(ClientRegistry::registerModelLoaders);
        ClientPlatformHelper.addBlockEntityRenderersRegistration(ClientRegistry::registerBlockEntityRenderers);
    }

    private static void registerTextures(ClientPlatformHelper.AtlasTextureEvent event) {
        //event.addSprite(PUMPKIN_HIGHLIGHT_TEXTURE);
        //event.addSprite(PUMPKIN_BACKGROUND_TEXTURE);
        event.addSprite(CARVED_PUMPKIN_BACKGROUND.texture());
        event.addSprite(CARVED_PUMPKIN_SHADE.texture());
        event.addSprite(JACK_O_LANTERN_SHADE_1.texture());
        event.addSprite(PUMPKIN_HIGHLIGHT.texture());
        event.addSprite(CARVING_OUTLINE.texture());
    }

    @EventCalled
    private static void registerBlockEntityRenderers(ClientPlatformHelper.BlockEntityRendererEvent event) {
        event.register(ModRegistry.MOD_CARVED_PUMPKIN_TILE.get(), CarvedPumpkinTileRenderer::new);
    }

    @EventCalled
    private static void registerModelLoaders(ClientPlatformHelper.ModelLoaderEvent event) {
        event.register(HarvestSeason.res("carved_pumpkin"), new CarvedPumpkinBlockLoader());
    }
}