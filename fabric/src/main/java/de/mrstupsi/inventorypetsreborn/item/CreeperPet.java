package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreeperPet extends InventoryPet {
    public static CreeperPet INSTANCE;

    public CreeperPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(20),
                4, 8,
                Items.GUNPOWDER
        );
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (source.isExplosive() && entity instanceof PlayerEntity) {
                PlayerEntity p = (PlayerEntity) entity;
                int activePet = InventoryPet.getActivePet(p, this);
                if (activePet != -1) {
                    if (!p.isCreative() && !p.isSpectator()) {
                        ItemStack is = p.getInventory().getStack(activePet);
                        is.setDamage(MathHelper.clamp(is.getDamage() + 1, 0, getMaxDamage()));
                        p.getInventory().setStack(activePet, is);
                    }
                    return false;
                } else return true;
            } else return true;
        });
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack is = hand == Hand.MAIN_HAND ? user.getMainHandStack() : user.getOffHandStack();
        if (InventoryPet.isActive(is)) {
            if (!user.isCreative() && !user.isSpectator()) {
                is.setDamage(MathHelper.clamp(is.getDamage() + 5, 0, getMaxDamage()));
                if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is);
                else user.getInventory().offHand.set(0, is);
            }
            user.world.createExplosion(user, user.getX(), user.getY(), user.getZ(), 3.0F, Explosion.DestructionType.BREAK);
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.creeper.explode"));
        tooltip.add(Text.translatable("tooltip.creeper.explosionresistance"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.gunpowder")));
        tooltip.add(Text.translatable("tooltip.mob"));
    }
}