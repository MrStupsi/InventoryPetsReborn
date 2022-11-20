package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GhastPet extends InventoryPet {
    public static GhastPet INSTANCE = new GhastPet();

    public GhastPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(25),
                5, 10,
                Items.BLAZE_POWDER
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
                Vec3 rot = user.getLookAngle();
                LargeFireball fireballEntity = new LargeFireball(world, user, rot.x, rot.y, rot.z, 1);
                fireballEntity.setPos(user.getX() + rot.x, user.getEyeY() + rot.y, user.getZ() + rot.z);
                world.addFreshEntity(fireballEntity);
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.ghast.fireball"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.blaze_powder")));
        tooltip.add(Component.translatable("tooltip.mob"));
    }
}