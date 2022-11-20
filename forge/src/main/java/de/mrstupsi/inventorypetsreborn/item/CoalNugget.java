package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class CoalNugget extends Item {
    public static CoalNugget INSTANCE = new CoalNugget();

    public CoalNugget() {
        super(new Properties().rarity(Rarity.COMMON).tab(InventoryPet.INVENTORY_PETS_GROUP).craftRemainder(Items.COAL));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return Items.COAL.getBurnTime(itemStack, recipeType) / 9;
    }
}