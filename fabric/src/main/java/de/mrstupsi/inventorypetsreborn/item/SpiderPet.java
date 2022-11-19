package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpiderPet extends InventoryPet {
    public static SpiderPet INSTANCE;

    public SpiderPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(30),
                5, 10,
                Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.RABBIT, Items.PORKCHOP
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity && InventoryPet.isActive(stack, slot)) {
            PlayerEntity p = (PlayerEntity) entity;
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 10, 3, false, false, false));
            if (Tick.TICK % (20 * 30) == 0) {
                p.giveItemStack(new ItemStack(Items.STRING));
            }
            if (Tick.TICK % (20 * 60) == 0 && !p.isCreative() && !p.isSpectator()) {
                stack.setDamage(MathHelper.clamp(stack.getDamage() + 1, 0, getMaxDamage()));
                p.getInventory().setStack(slot, stack);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.spider.jumpboost"));
        tooltip.add(Text.translatable("tooltip.spider.wallclimb"));
        tooltip.add(Text.translatable("tooltip.spider.givesstring"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.raw_meat")));
        tooltip.add(Text.translatable("tooltip.mob"));
    }
}