package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class ObsidianNugget extends Item {
    public static ObsidianNugget INSTANCE = new ObsidianNugget();

    public ObsidianNugget() {
        super(new Properties().rarity(Rarity.COMMON).tab(InventoryPet.INVENTORY_PETS_GROUP).craftRemainder(Items.OBSIDIAN));
    }
}