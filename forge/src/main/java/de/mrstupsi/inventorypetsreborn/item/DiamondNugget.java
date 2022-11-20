package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class DiamondNugget extends Item {
    public static DiamondNugget INSTANCE = new DiamondNugget();

    public DiamondNugget() {
        super(new Properties().rarity(Rarity.COMMON).tab(InventoryPet.INVENTORY_PETS_GROUP).craftRemainder(Items.DIAMOND));
    }
}