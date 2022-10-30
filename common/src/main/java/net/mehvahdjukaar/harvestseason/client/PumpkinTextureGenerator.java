package net.mehvahdjukaar.harvestseason.client;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.harvestseason.reg.ClientRegistry;
import net.minecraft.client.resources.model.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PumpkinTextureGenerator {

    /**
     * Turns carved pixels matrix into a usable color matrix for the carved section of a pumpkin
     * Rest of the texture is simply using vanilla texture
     */
    public static Material[][] getTexturePerPixel(boolean[][] pixels, boolean jackOLantern) {
        Type[][] colors = new Type[16][16];

        forEachPixel(colors, (j, i) -> {
            if (!pixels[j][i]) {
                colors[j][i] = Type.UNCARVED;
            } else {
                if (shouldShade(colors, j, i)) {
                    colors[j][i] = Type.SHADE;
                } else {
                    colors[j][i] = Type.BACKGROUND;
                }
            }
        });

        addExtraShade(colors);
        forEachPixel(colors, (j, i) -> {
            addHighlight(colors, j, i);
        });
        Material[][] materials = new Material[16][16];
        if (jackOLantern) {
            mapJackOLanternMaterials(colors, materials);
        } else {
            mapCarvedMaterials(colors, materials);
        }
        return materials;
    }

    private static void mapCarvedMaterials(Type[][] colors, Material[][] materials) {
        forEachPixel(materials, (j, i) -> materials[j][i] = switch (colors[j][i]) {
                    case UNCARVED -> ClientRegistry.PUMPKIN;
                    case SHADE -> ClientRegistry.CARVED_PUMPKIN_SHADE;
                    case BACKGROUND -> ClientRegistry.CARVED_PUMPKIN_BACKGROUND;
                    case HIGHLIGHT -> ClientRegistry.PUMPKIN_HIGHLIGHT;
                }
        );
    }

    private static void mapJackOLanternMaterials(Type[][] colors, Material[][] materials) {
        forEachPixel(materials, (j, i) -> materials[j][i] = switch (colors[j][i]) {
                    case UNCARVED -> ClientRegistry.PUMPKIN;
                    case SHADE -> ClientRegistry.JACK_O_LANTERN_SHADE_1;
                    case BACKGROUND -> ClientRegistry.JACK_O_LANTERN_BACKGROUND;
                    case HIGHLIGHT -> ClientRegistry.PUMPKIN_HIGHLIGHT;
                }
        );
    }

    private static void addExtraShade(Type[][] px) {
        List<Pair<Integer, Integer>> shades = new ArrayList<>();
        forEachPixel(px, (j, i) -> {
            if (!isUnCarved(px, j, i)) {
                //Up
                if (isShaded(px, j, i - 1)) {
                    //sides
                    if (isShaded(px, j - 1, i) && isShaded(px, j + 1, i)) {
                        if (isShaded(px, j - 1, i + 1) || isShaded(px, j + 1, i + 1)) {
                            shades.add(Pair.of(j, i));
                        }
                    }
                    if (isShaded(px, j, i + 1)) {
                        if (isShaded(px, j + 1, i - 1) && isShaded(px, j - 1, i) ||
                                isShaded(px, j - 1, i - 1) && isShaded(px, j + 1, i)) {
                            shades.add(Pair.of(j, i));
                        }
                    }
                }
            }
        });
        shades.forEach(p -> px[p.getFirst()][p.getSecond()] = Type.SHADE);
    }


    private static void addHighlight(Type[][] px, int j, int i) {
        if (isUnCarved(px, j, i)) {
            if (!isUnCarved(px, j - 1, i) || (!isUnCarved(px, j, i - 1))) {
                px[j][i] = Type.HIGHLIGHT;
            }
        }
    }

    private static boolean shouldShade(Type[][] px, int j, int i) {
        return (isUnCarved(px, j - 1, i) || isUnCarved(px, j, i - 1));
    }

    private static boolean isUnCarved(Type[][] px, int j, int i) {
        if (j < 0 || i < 0 || j > 15 || i > 15) return true;
        var t = px[j][i];
        return t == Type.UNCARVED || t == Type.HIGHLIGHT;
    }

    private static boolean isShaded(Type[][] px, int j, int i) {
        if (j < 0 || i < 0 || j > 15 || i > 15) return true;
        return px[j][i] == Type.SHADE;
    }

    public static void forEachPixel(Object[][] px, BiConsumer<Integer, Integer> function) {
        for (int j = 0; j < px.length; j++) {
            for (int i = 0; i < px[j].length; i++) {
                function.accept(j, i);
            }
        }
    }

    public enum Type {
        UNCARVED,
        BACKGROUND,
        SHADE,
        HIGHLIGHT
    }
}
