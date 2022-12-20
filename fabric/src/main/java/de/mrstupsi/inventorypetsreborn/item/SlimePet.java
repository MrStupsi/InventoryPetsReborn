package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlimePet extends InventoryPet {
    public static SlimePet INSTANCE;

    public SlimePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(2),
                1, 1,
                Items.GOLDEN_APPLE
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.slime.revive"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.golden_apple")));
        tooltip.add(Text.translatable("tooltip.legendary"));
    }
}