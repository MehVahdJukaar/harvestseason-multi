package net.mehvahdjukaar.harvestseason.blocks;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.compat.SuppCompat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CandyCornItem extends Item {
    public CandyCornItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if( HarvestSeason.SUPP_INSTALLED) SuppCompat.triggerSweetTooth(level, entity);
        return super.finishUsingItem(stack, level, entity);
    }
}
