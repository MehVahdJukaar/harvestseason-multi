package net.mehvahdjukaar.harvestseason.reg;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.blocks.*;
import net.mehvahdjukaar.harvestseason.items.ModCarvedPumpkinItem;
import net.mehvahdjukaar.harvestseason.items.crafting.JackOLanternRecipe;
import net.mehvahdjukaar.harvestseason.items.crafting.PumpkinDuplicateRecipe;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"unused"})
public class ModRegistry {


    public static void init() {
    }


    public static final Supplier<RecipeSerializer<PumpkinDuplicateRecipe>> PUMPKIN_DUPLICATE_RECIPE = regRecipe(
            "carved_pumpkin_duplicate", PumpkinDuplicateRecipe::new);

    public static final Supplier<RecipeSerializer<JackOLanternRecipe>> JACK_O_LANTERN_RECIPE = regRecipe(
            "jack_o_lantern", JackOLanternRecipe::new);

    public static final Supplier<Block> CORN_BASE = regBlock("corn_base", () -> new CornBaseBlock(
            BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)
                    .randomTicks()
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .instabreak()
                    .sound(SoundType.CROP))
    );

    public static final Supplier<Block> CORN_MIDDLE = regBlock("corn_middle", () -> new CornMiddleBlock(
            BlockBehaviour.Properties.copy(CORN_BASE.get()))
    );

    public static final Supplier<Block> CORN_TOP = regBlock("corn_top", () -> new CornTobBlock(
            BlockBehaviour.Properties.copy(CORN_BASE.get()))
    );

    public static final Supplier<Item> COB_ITEM = regItem("corn", () -> new Item(
            (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));

    public static final Supplier<Item> COOKED_COB = regItem("corn_on_the_cob", () -> new Item(
            (new Item.Properties()).tab(CreativeModeTab.TAB_FOOD).food(ModFood.CORN_ON_THE_COB)));

    public static final Supplier<Item> POP_CORN = regItem("popcorn", () -> new Item(
            (new Item.Properties()).tab(CreativeModeTab.TAB_FOOD).food(ModFood.POPCORN)));

    public static final Supplier<Item> CANDY_CORN = regItem("candy_corn", () -> new Item(
            (new Item.Properties()).tab(CreativeModeTab.TAB_FOOD).food(ModFood.CANDY_CORN)));


    public static final Supplier<Block> PAPER_BAG = regWithItem("paper_bag", () -> new PaperBagBlock(
            BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)), CreativeModeTab.TAB_MISC);

    public static final Supplier<Block> CANDY_BAG = regBlock("candy_bag", () -> new CandyBagBlock(
            BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));

    public static final Supplier<BlockEntityType<CandyBagTile>> CANDY_BAG_TILE = regTile(
            "candy_bag", () -> PlatformHelper.newBlockEntityType(
                    CandyBagTile::new, CANDY_BAG.get()));

    public static final Supplier<Item> CORN_SEEDS = regItem("kernels", () -> new ItemNameBlockItem(CORN_BASE.get(),
            (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));


    public static final Supplier<Block> MOD_CARVED_PUMPKIN = regBlock("carved_pumpkin",
            () -> new ModCarvedPumpkinBlock(BlockBehaviour.Properties.copy(Blocks.CARVED_PUMPKIN)));

    public static final Supplier<Item> MOD_CARVED_PUMPKIN_ITEM = regItem("carved_pumpkin",
            () -> new ModCarvedPumpkinItem(MOD_CARVED_PUMPKIN.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));


    public static final Supplier<Block> MOD_JACK_O_LANTERN = regBlock("jack_o_lantern",
            () -> new ModCarvedPumpkinBlock(BlockBehaviour.Properties.copy(Blocks.CARVED_PUMPKIN)
                    .lightLevel(s -> 15)));

    public static final Supplier<Item> MOD_JACK_O_LANTERN_ITEM = regItem("jack_o_lantern",
            () -> new ModCarvedPumpkinItem(MOD_JACK_O_LANTERN.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));


    public static final Supplier<BlockEntityType<ModCarvedPumpkinBlockTile>> MOD_CARVED_PUMPKIN_TILE =
            regTile("carved_pumpkin", () ->
                    PlatformHelper.newBlockEntityType(ModCarvedPumpkinBlockTile::new,
                            MOD_CARVED_PUMPKIN.get(), MOD_JACK_O_LANTERN.get()));


    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> sup) {
        return RegHelper.registerItem(HarvestSeason.res(name), sup);
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> regTile(String name, Supplier<T> sup) {
        return RegHelper.registerBlockEntityType(HarvestSeason.res(name), sup);
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> sup) {
        return RegHelper.registerBlock(HarvestSeason.res(name), sup);
    }


    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory, CreativeModeTab tab) {
        return regWithItem(name, blockFactory, new Item.Properties().tab(getTab(tab, name)), 0);
    }

    private static CreativeModeTab getTab(CreativeModeTab tab, String name) {
        return tab;
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory, CreativeModeTab tab, int burnTime) {
        return regWithItem(name, blockFactory, new Item.Properties().tab(getTab(tab, name)), burnTime);
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory, Item.Properties properties, int burnTime) {
        Supplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, properties, burnTime);
        return block;
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> block, CreativeModeTab tab, String requiredMod) {
        CreativeModeTab t = PlatformHelper.isModLoaded(requiredMod) ? tab : null;
        return regWithItem(name, block, t);
    }

    public static Supplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties, int burnTime) {
        return RegHelper.registerItem(HarvestSeason.res(name), () -> burnTime == 0 ? new BlockItem(blockSup.get(), properties) :
                new WoodBasedBlockItem(blockSup.get(), properties, burnTime));
    }

    private static <T extends Recipe<?>> Supplier<RecipeSerializer<T>> regRecipe(String name, Function<ResourceLocation, T> factory) {
        return RegHelper.registerRecipeSerializer(HarvestSeason.res(name), () -> new SimpleRecipeSerializer<>(factory));
    }

}
