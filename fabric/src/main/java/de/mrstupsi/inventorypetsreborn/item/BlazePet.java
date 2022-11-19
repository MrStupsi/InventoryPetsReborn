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

public class BlazePet extends InventoryPet {
    public static BlazePet INSTANCE;

    public BlazePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(30),
                5, 10,
                Items.QUARTZ
        );
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (source.getAttacker() != null &&
                    InventoryPet.hasActivePet(source.getAttacker(), this)) {
                entity.setFireTicks(20 * 5);
            }
            return true;
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity && InventoryPet.isActive(stack, slot)) {
            PlayerEntity p = (PlayerEntity) entity;
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 0, false, false, false));
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamage(MathHelper.clamp(stack.getDamage() + 1, 0, getMaxDamage()));
                p.getInventory().setStack(slot, stack);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.blaze.strength"));
        tooltip.add(Text.translatable("tooltip.blaze.firedamage"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.quartz")));
        tooltip.add(Text.translatable("tooltip.mob"));
    }
}