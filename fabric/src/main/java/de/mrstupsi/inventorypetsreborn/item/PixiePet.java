package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PixiePet extends InventoryPet {
    public static PixiePet INSTANCE;

    public PixiePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(80),
                15, 30,
                EmeraldNugget.INSTANCE
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.pixie.double_exp"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.emerald_nugget")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}