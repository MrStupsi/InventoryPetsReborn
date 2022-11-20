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

public class PigPet extends InventoryPet {
    public static PigPet INSTANCE = new PigPet();

    public PigPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(20),
                4, 8,
                Items.CARROT
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player && InventoryPet.isActive(stack, slot)) {
            Player p = (Player) entity;
            if (Tick.TICK % (20 * 30) == 0) {
                p.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0, false, false, false));
                p.getInventory().add(new ItemStack(Items.PORKCHOP));
            }
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamageValue(Math.min(Math.max(stack.getDamageValue() + 1, 0), stack.getMaxDamage()));
                p.getInventory().setItem(slot, stack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.pig.saturation"));
        tooltip.add(Component.translatable("tooltip.pig.removespoisinouseffects"));
        tooltip.add(Component.translatable("tooltip.pig.givesporkchop"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.carrot")));
        tooltip.add(Component.translatable("tooltip.friendly"));
    }
}