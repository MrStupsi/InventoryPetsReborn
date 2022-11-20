package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class LapisNugget extends Item {
    public static LapisNugget INSTANCE = new LapisNugget();

    public LapisNugget() {
        super(new Properties().rarity(Rarity.COMMON).tab(InventoryPet.INVENTORY_PETS_GROUP).craftRemainder(Items.LAPIS_LAZULI));
    }
}