package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class EmeraldNugget extends Item {
    public static EmeraldNugget INSTANCE = new EmeraldNugget();

    public EmeraldNugget() {
        super(new Item.Properties().rarity(Rarity.COMMON).tab(InventoryPet.INVENTORY_PETS_GROUP).craftRemainder(Items.EMERALD));
    }
}