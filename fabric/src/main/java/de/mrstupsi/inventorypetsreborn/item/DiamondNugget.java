package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class DiamondNugget extends Item {
    public static DiamondNugget INSTANCE;

    public DiamondNugget() {
        super(new Settings().rarity(Rarity.COMMON).group(InventoryPet.INVENTORY_PETS_GROUP).recipeRemainder(Items.DIAMOND));
    }
}