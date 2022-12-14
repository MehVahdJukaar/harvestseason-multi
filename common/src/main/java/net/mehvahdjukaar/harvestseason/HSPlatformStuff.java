package net.mehvahdjukaar.harvestseason;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlockTile;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Contract;

public class HSPlatformStuff {
    @Contract
    @ExpectPlatform
    public static boolean isTopCarver(ItemStack stack) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void addPumpkinData(ModCarvedPumpkinBlockTile tile, SnowGolem snowGolem) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ShaderInstance getBlur() {
        throw new AssertionError();
    }
}
