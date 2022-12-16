package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlazePet extends InventoryPet {
    public static BlazePet INSTANCE = new BlazePet();

    public BlazePet() {
        super(
                new Item.Properties().rarity(Rarity.COMMON).durability(30),
                5, 10,
                PetType.MOB,
                Items.QUARTZ
        );
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player && InventoryPet.isActive(stack, slot)) {
            Player p = (Player) entity;
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10, 0, false, false, false));
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamageValue(Math.min(Math.max(stack.getDamageValue() + 1, 0), stack.getMaxDamage()));
                p.getInventory().setItem(slot, stack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.blaze.strength"));
        tooltip.add(Component.translatable("tooltip.blaze.firedamage"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.quartz")));
        tooltip.add(getType().getTooltip());
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent e) {
        if (e.getSource().getEntity() != null &&
                InventoryPet.hasActivePet(e.getSource().getEntity(), this)) {
            e.getEntity().setRemainingFireTicks(20 * 5);
        }
    }
}