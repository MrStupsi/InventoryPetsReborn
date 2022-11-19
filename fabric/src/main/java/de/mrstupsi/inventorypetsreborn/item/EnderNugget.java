package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class EnderNugget extends Item {
    public static EnderNugget INSTANCE;

    public EnderNugget() {
        super(new Settings().rarity(Rarity.COMMON).group(InventoryPet.INVENTORY_PETS_GROUP).recipeRemainder(Items.ENDER_PEARL));
    }
}