package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import de.mrstupsi.inventorypetsreborn.mixin.LivingEntityMixin;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CowPet extends InventoryPet {
    public static CowPet INSTANCE;

    public CowPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(20),
                4, 8,
                Items.WHEAT
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
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity && InventoryPet.isActive(stack)) {
            PlayerEntity p = (PlayerEntity) entity;
            if (Tick.TICK % (20 * 60) == 0) {
                for (int i = 0; i < 9; i++) {
                    ItemStack is = p.getInventory().getStack(i);
                    if (is != null && is.getItem() == Items.BUCKET) {
                        if (p.giveItemStack(new ItemStack(Items.MILK_BUCKET))) {
                            if (!p.isCreative() && !p.isSpectator()) {
                                stack.setDamage(MathHelper.clamp(stack.getDamage() + 2, 0, getMaxDamage()));
                                p.getInventory().setStack(slot, stack);
                            }
                            is.setCount(is.getCount() - 1);
                            p.getInventory().setStack(i, is);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.cow.removesnegativeeffects"));
        tooltip.add(Text.translatable("tooltip.cow.fillsbuckets"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.wheat")));
        tooltip.add(Text.translatable("tooltip.friendly"));
    }
}