package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MooshroomPet extends InventoryPet {
    public static MooshroomPet INSTANCE;

    public MooshroomPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(30),
                5, 10,
                Items.WHEAT
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity && InventoryPet.isActive(stack)) {
            PlayerEntity p = (PlayerEntity) entity;
            if (Tick.TICK % (20 * 60) == 0) {
                for (int i = 0; i < 9; i++) {
                    ItemStack is = p.getInventory().getStack(i);
                    if (is != null && is.getItem() == Items.BOWL) {
                        if (p.giveItemStack(new ItemStack(Items.MUSHROOM_STEW))) {
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
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack is = context.getStack();
        PlayerEntity p = context.getPlayer();
        Hand h = context.getHand();
        World w = context.getWorld();
        if (InventoryPet.isActive(is)) {
            if (!w.isClient && BoneMealItem.useOnFertilizable(is.copy(), w, context.getBlockPos()) &&
                    !p.isCreative() && !p.isSpectator()) {
                is.setDamage(MathHelper.clamp(is.getDamage() + 5, 0, getMaxDamage()));
                if (h == Hand.MAIN_HAND) p.getInventory().main.set(p.getInventory().selectedSlot, is);
                else p.getInventory().offHand.set(0, is);
            }
            return ActionResult.success(true);
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.mooshroom.growplants"));
        tooltip.add(Text.translatable("tooltip.mooshroom.fillsbowls"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.wheat")));
        tooltip.add(Text.translatable("tooltip.friendly"));
    }
}