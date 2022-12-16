package de.mrstupsi.inventorypetsreborn.item;

import de.mrstupsi.inventorypetsreborn.Tick;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MooshroomPet extends InventoryPet {
    public static MooshroomPet INSTANCE = new MooshroomPet();

    public MooshroomPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(30),
                5, 10,
                PetType.FRIENDLY,
                Items.WHEAT
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player && InventoryPet.isActive(stack)) {
            Player p = (Player) entity;
            if (Tick.TICK % (20 * 60) == 0) {
                for (int i = 0; i < 9; i++) {
                    ItemStack is = p.getInventory().getItem(i);
                    if (is != null && is.getItem() == Items.BOWL) {
                        if (p.getInventory().add(new ItemStack(Items.MUSHROOM_STEW))) {
                            if (!p.isCreative() && !p.isSpectator()) {
                                stack.setDamageValue(Math.min(Math.max(stack.getDamageValue() + 2, 0), stack.getMaxDamage()));
                                p.getInventory().setItem(slot, stack);
                            }
                            is.setCount(is.getCount() - 1);
                            p.getInventory().setItem(i, is);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack is = context.getItemInHand();
        Player p = context.getPlayer();
        InteractionHand h = context.getHand();
        Level w = context.getLevel();
        if (InventoryPet.isActive(is)) {
            if (!w.isClientSide && BoneMealItem.applyBonemeal(is.copy(), w, context.getClickedPos(), p) &&
                    !p.isCreative() && !p.isSpectator()) {
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 5, 0), is.getMaxDamage()));
                if (h == InteractionHand.MAIN_HAND) p.getInventory().setItem(p.getInventory().selected, is);
                else p.getInventory().offhand.set(0, is);
            }
            return InteractionResult.sidedSuccess(true);
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.mooshroom.growplants"));
        tooltip.add(Component.translatable("tooltip.mooshroom.fillsbowls"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.wheat")));
        tooltip.add(getType().getTooltip());
    }
}