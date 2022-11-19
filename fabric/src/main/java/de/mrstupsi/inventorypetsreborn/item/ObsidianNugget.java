package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class ObsidianNugget extends Item {
    public static ObsidianNugget INSTANCE;

    public ObsidianNugget() {
        super(new Settings().rarity(Rarity.COMMON).group(InventoryPet.INVENTORY_PETS_GROUP).recipeRemainder(Items.OBSIDIAN));
    }
}