package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PigPet extends InventoryPet {
    public static PigPet INSTANCE;

    public PigPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(20),
                4, 8,
                Items.CARROT
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity && InventoryPet.isActive(stack, slot)) {
            PlayerEntity p = (PlayerEntity) entity;
            if (Tick.TICK % (20 * 30) == 0) {
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 1, 0, false, false, false));
                p.giveItemStack(new ItemStack(Items.PORKCHOP));
            }
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamage(MathHelper.clamp(stack.getDamage() + 1, 0, getMaxDamage()));
                p.getInventory().setStack(slot, stack);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.pig.saturation"));
        tooltip.add(Text.translatable("tooltip.pig.removespoisinouseffects"));
        tooltip.add(Text.translatable("tooltip.pig.givesporkchop"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.carrot")));
        tooltip.add(Text.translatable("tooltip.friendly"));
    }
}