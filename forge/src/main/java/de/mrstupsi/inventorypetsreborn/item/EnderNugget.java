package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class EnderNugget extends Item {
    public static EnderNugget INSTANCE = new EnderNugget();

    public EnderNugget() {
        super(new Properties().rarity(Rarity.COMMON).tab(InventoryPet.INVENTORY_PETS_GROUP).craftRemainder(Items.ENDER_PEARL));
    }
}