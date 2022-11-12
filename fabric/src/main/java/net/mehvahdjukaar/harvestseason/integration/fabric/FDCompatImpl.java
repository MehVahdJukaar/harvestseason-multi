package net.mehvahdjukaar.harvestseason.integration.fabric;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.item.ConsumableItem;
import com.nhoryzon.mc.farmersdelight.registry.EffectsRegistry;
import net.mehvahdjukaar.harvestseason.reg.ModFood;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

import static net.mehvahdjukaar.harvestseason.reg.ModRegistry.regItem;
import static net.mehvahdjukaar.harvestseason.reg.ModRegistry.regWithItem;

public class FDCompatImpl {
    public static void init() {
    }

    public static final FoodProperties SUCCOTASH_FOOD = new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(0.8F)
            .effect(new MobEffectInstance(EffectsRegistry.NOURISHMENT.get(), 3600, 0), 1.0F)
            .build();

    public static final Supplier<Block> CORN_CRATE = regWithItem(
            "corn_crate", () ->
                    new Block(BlockBehaviour.Properties.of(Material.WOOD)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)),
            FarmersDelightMod.ITEM_GROUP
    );
    public static final Supplier<Item> CORNBREAD = regItem(
            "cornbread", () -> new ConsumableItem(new Item.Properties().tab(FarmersDelightMod.ITEM_GROUP)
                    .food(ModFood.CORNBREAD), false)
    );
    public static final Supplier<Item> SUCCOTASH = regItem(
            "succotash", () -> new ConsumableItem(bowlFoodItem(SUCCOTASH_FOOD), true)
    );

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelightMod.ITEM_GROUP);
    }
}
