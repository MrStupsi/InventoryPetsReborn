package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootPet extends InventoryPet {
    public static LootPet INSTANCE = new LootPet();

    public LootPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(60),
                10, 20,
                Items.GOLD_NUGGET
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.loot.double_ores"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.gold_nugget")));
        tooltip.add(Component.translatable("tooltip.neutral"));
    }
}