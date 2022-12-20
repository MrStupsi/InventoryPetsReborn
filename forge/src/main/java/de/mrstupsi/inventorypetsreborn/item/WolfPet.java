package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WolfPet extends InventoryPet {
    public static WolfPet INSTANCE = new WolfPet();

    public WolfPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(3),
                1, 2,
                PetType.FRIENDLY,
                Items.BONE
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack is = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
        if (InventoryPet.isActive(is)) {
            if (!user.isCreative() && !user.isSpectator()) {
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 5, 0), is.getMaxDamage()));
                if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                else user.getInventory().offhand.set(0, is);
            }
            if (!world.isClientSide) {
                Wolf w = new Wolf(EntityType.WOLF, world);
                world.addFreshEntity(w);
                Component name = user.getName().copy();
                name.getSiblings().add(Component.literal("'s Dog"));
                w.setCustomName(name);
                w.tame(user);
                w.teleportTo(user.getX(), user.getY(), user.getZ());
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.wolf.spawn_wolf"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.bone")));
        tooltip.add(getType().getTooltip());
    }
}