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

public class SpiderPet extends InventoryPet {
    public static SpiderPet INSTANCE = new SpiderPet();

    public SpiderPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(30),
                5, 10,
                PetType.MOB,
                Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.RABBIT, Items.PORKCHOP
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player && InventoryPet.isActive(stack, slot)) {
            Player p = (Player) entity;
            p.addEffect(new MobEffectInstance(MobEffects.JUMP, 10, 3, false, false, false));
            if (Tick.TICK % (20 * 30) == 0) {
                p.getInventory().add(new ItemStack(Items.STRING));
            }
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamageValue(Math.min(Math.max(stack.getDamageValue() + 1, 0), stack.getMaxDamage()));
                p.getInventory().setItem(slot, stack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.spider.jumpboost"));
        tooltip.add(Component.translatable("tooltip.spider.wallclimb"));
        tooltip.add(Component.translatable("tooltip.spider.givesstring"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.raw_meat")));
        tooltip.add(getType().getTooltip());
    }
}