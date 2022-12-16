package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PixiePet extends InventoryPet {
    public static PixiePet INSTANCE = new PixiePet();

    public PixiePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(80),
                15, 30,
                PetType.NEUTRAL,
                EmeraldNugget.INSTANCE
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.pixie.double_exp"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.emerald_nugget")));
        tooltip.add(getType().getTooltip());
    }
}