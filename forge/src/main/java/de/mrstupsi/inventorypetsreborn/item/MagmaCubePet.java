package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagmaCubePet extends InventoryPet {
    public static MagmaCubePet INSTANCE = new MagmaCubePet();

    public MagmaCubePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(30),
                5, 10,
                PetType.MOB,
                Items.QUARTZ
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player && InventoryPet.isActive(stack, slot)) {
            Player p = (Player) entity;
            p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10, 0, false, false, false));
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamageValue(Math.min(Math.max(stack.getDamageValue() + 1, 0), stack.getMaxDamage()));
                p.getInventory().setItem(slot, stack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.magmacube.fireresistance"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.quartz")));
        tooltip.add(getType().getTooltip());
    }
}