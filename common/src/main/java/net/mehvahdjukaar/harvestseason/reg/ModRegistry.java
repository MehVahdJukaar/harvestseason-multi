package net.mehvahdjukaar.harvestseason.reg;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.blocks.CornBaseBlock;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlock;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlockTile;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

@SuppressWarnings({"unused"})
public class ModRegistry {


    public static void init() {
    }


    public static final Supplier<Block> CORN_BASE = regBlock("corn_base", () -> new CornBaseBlock(
            BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP))
    );

    public static final Supplier<Block> CORN_MIDDLE = regBlock("corn_midle", () -> new CornBaseBlock(
            BlockBehaviour.Properties.copy(CORN_BASE.get()))
    );

    public static final Supplier<Block> CORN_TOP = regBlock("corn_top", () -> new CornBaseBlock(
            BlockBehaviour.Properties.copy(CORN_BASE.get()))
    );

    public static final Supplier<Item> COB_ITEM = regItem("cob", () -> new Item(
            (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));

    public static final Supplier<Item> CORN_SEEDS = regItem("corn_seeds", () -> new ItemNameBlockItem(CORN_BASE.get(),
            (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));


    public static final Supplier<Block> MOD_CARVED_PUMPKIN = regWithItem("carved_pumpkin",
            () -> new ModCarvedPumpkinBlock(BlockBehaviour.Properties.copy(Blocks.CARVED_PUMPKIN)),
            CreativeModeTab.TAB_DECORATIONS);

    public static final Supplier<Block> MOD_JACK_O_LANTERN = regWithItem("jack_o_lantern",
            () -> new ModCarvedPumpkinBlock(BlockBehaviour.Properties.copy(Blocks.CARVED_PUMPKIN)),
            CreativeModeTab.TAB_DECORATIONS);

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

}
