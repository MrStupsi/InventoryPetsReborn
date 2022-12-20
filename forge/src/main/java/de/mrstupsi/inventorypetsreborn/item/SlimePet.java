package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlimePet extends InventoryPet {
    public static SlimePet INSTANCE = new SlimePet();

    public SlimePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(2),
                1, 1,
                PetType.LEGENDARY,
                Items.GOLDEN_APPLE
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.slime.revive"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.golden_apple")));
        tooltip.add(getType().getTooltip());
    }
}