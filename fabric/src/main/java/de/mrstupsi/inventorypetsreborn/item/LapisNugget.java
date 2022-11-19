package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class LapisNugget extends Item {
    public static LapisNugget INSTANCE;

    public LapisNugget() {
        super(new Settings().rarity(Rarity.COMMON).group(InventoryPet.INVENTORY_PETS_GROUP).recipeRemainder(Items.LAPIS_LAZULI));
    }
}