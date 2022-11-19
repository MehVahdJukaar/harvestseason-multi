package net.mehvahdjukaar.harvestseason.items.crafting;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ModCarvedPumpkinRecipe extends CustomRecipe {
    public ModCarvedPumpkinRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    private boolean isFilled(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        return tag != null && tag.contains("Pixels");
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {

        ItemStack itemstack = null;
        ItemStack itemstack1 = null;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (item == ModRegistry.MOD_CARVED_PUMPKIN.get().asItem() && itemstack == null) {
                if (isFilled(stack)) {
                    itemstack = stack;
                }
            }else if(item == Items.TORCH || stack.is(HarvestSeason.CARVABLE_PUMPKINS)){
                if (itemstack1 != null) {
                    return false;
                }

                itemstack1 = stack;
            }
            else if (!stack.isEmpty()) return false;
        }

        return itemstack != null && itemstack1 != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        boolean torch = false;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            if(inv.getItem(i).getItem() == Items.TORCH)torch = true;
        }
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (isFilled(stack)) {
                ItemStack s =(torch ? ModRegistry.MOD_JACK_O_LANTERN_ITEM : ModRegistry.MOD_CARVED_PUMPKIN_ITEM).get().getDefaultInstance();
                s.setCount(1);
                s.setTag(stack.getTag().copy());
                return s;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (!itemstack.isEmpty()) {
                if (isFilled(itemstack)) {
                    ItemStack copy = itemstack.copy();
                    copy.setCount(1);
                    stacks.set(i, copy);
                    return stacks;
                }
            }
        }
        return stacks;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.CARVED_PUMPKIN_RECIPE.get();
    }


}
