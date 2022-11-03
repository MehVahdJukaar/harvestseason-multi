package net.mehvahdjukaar.harvestseason.client;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlockTile;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CarvingManager {

    private static final TextureManager TEXTURE_MANAGER = Minecraft.getInstance().getTextureManager();

    private static final LoadingCache<Key, Carving> TEXTURE_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .removalListener(i -> {
                Carving value = (Carving) i.getValue();
                if (value != null) {
                    RenderSystem.recordRenderCall(value::close);
                }
            })
            .build(new CacheLoader<>() {
                @Override
                public Carving load(Key key) {
                    return null;
                }
            });

    public static Carving getInstance(Key key) {
        Carving textureInstance = TEXTURE_CACHE.getIfPresent(key);
        if (textureInstance == null) {
            textureInstance = new Carving(ModCarvedPumpkinBlockTile.unpackPixels(key.values), key.glow);
            TEXTURE_CACHE.put(key, textureInstance);
        }
        return textureInstance;
    }

    @Immutable
    public static class Key implements TooltipComponent {
        private final long[] values;
        private final boolean glow;

        Key(long[] packed, boolean glowing) {
            values = packed;
            glow = glowing;
        }

        public static Key of(long[] packPixels, boolean glowing) {
            return new Key(packPixels, glowing);
        }

        public static Key of(long[] packPixels) {
            return new Key(packPixels, false);
        }

        @Override
        public boolean equals(Object another) {
            if (another == this) {
                return true;
            }
            if (another == null) {
                return false;
            }
            if (another.getClass() != this.getClass()) {
                return false;
            }
            Key key = (Key) another;
            return Arrays.equals(this.values, key.values) && glow == key.glow;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.values);
        }
    }


    public static class Carving implements AutoCloseable {
        private static final int WIDTH = 16;

        //models for each direction
        private final Map<Direction, List<BakedQuad>> quadsCache = new EnumMap<>(Direction.class);
        private final boolean[][] pixels;
        private final boolean glow;
        //he is lazy
        @Nullable
        private DynamicTexture texture;
        @Nullable
        private RenderType renderType;
        @Nullable
        private ResourceLocation textureLocation;

        private Carving(boolean[][] pixels, boolean glow) {
            this.pixels = pixels;
            this.glow = glow;
        }

        public boolean[][] getPixels() {
            return pixels;
        }

        public boolean isGlow() {
            return glow;
        }

        //cant initialize right away since this texture can be created from worked main tread during model bake since it needs getQuads

        private void initializeTexture() {
            this.texture = new DynamicTexture(WIDTH, WIDTH, false);

            Material[][] materials = PumpkinTextureGenerator.getTexturePerPixel(this.pixels, this.glow);

            for (int y = 0; y < pixels.length && y < WIDTH; y++) {
                for (int x = 0; x < pixels[y].length && x < WIDTH; x++) { //getColoredPixel(BlackboardBlock.colorFromByte(pixels[x][y]),x,y)
                   int c = ClientPlatformHelper.getPixelRGBA(materials[x][y].sprite(),0, x, y);

                    this.texture.getPixels().setPixelRGBA(x, y, c);
                }
            }
            this.texture.upload();

            //texture manager has its own internal id
            this.textureLocation = TEXTURE_MANAGER.register("blackboard/", this.texture);
            this.renderType = RenderType.entitySolid(textureLocation);
        }

        @Nonnull
        public List<BakedQuad> getOrCreateModel(Direction dir, Supplier<List<BakedQuad>> modelFactory) {
            return this.quadsCache.computeIfAbsent(dir, d -> modelFactory.get());
        }

        @Nonnull
        public ResourceLocation getTextureLocation() {
            if (textureLocation == null) {
                //I can only initialize it here since this is guaranteed to be on render thread
                this.initializeTexture();
            }
            return textureLocation;
        }

        @Nonnull
        public RenderType getRenderType() {
            if (renderType == null) {
                //I can only initialize it here since this is guaranteed to be on render thread
                this.initializeTexture();
            }
            return renderType;
        }

        //should be called when cache expires
        @Override
        public void close() {
            if (texture != null) this.texture.close();
            if (textureLocation != null) TEXTURE_MANAGER.release(textureLocation);
        }
    }

}

