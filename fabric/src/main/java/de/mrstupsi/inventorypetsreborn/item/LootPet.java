package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootPet extends InventoryPet {
    public static LootPet INSTANCE;

    public LootPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(60),
                10, 20,
                Items.GOLD_NUGGET
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.loot.double_ores"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.gold_nugget")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}