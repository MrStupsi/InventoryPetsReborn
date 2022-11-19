package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class EmeraldNugget extends Item {
    public static EmeraldNugget INSTANCE;

    public EmeraldNugget() {
        super(new Settings().rarity(Rarity.COMMON).group(InventoryPet.INVENTORY_PETS_GROUP).recipeRemainder(Items.EMERALD));
    }
}