package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class CoalNugget extends Item {
    public static CoalNugget INSTANCE;

    public CoalNugget() {
        super(new Settings().rarity(Rarity.COMMON).group(InventoryPet.INVENTORY_PETS_GROUP).recipeRemainder(Items.COAL));
        FuelRegistry.INSTANCE.add(this, FuelRegistry.INSTANCE.get(Items.COAL) / 9);
    }
}