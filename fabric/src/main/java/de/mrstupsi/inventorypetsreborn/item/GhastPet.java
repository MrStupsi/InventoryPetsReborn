package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GhastPet extends InventoryPet {
    public static GhastPet INSTANCE;

    public GhastPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(25),
                5, 10,
                Items.BLAZE_POWDER
        );
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
            if (!world.isClient) {
                Vec3d rot = user.getRotationVector();
                FireballEntity fireballEntity = new FireballEntity(world, user, rot.x, rot.y, rot.z, 1);
                fireballEntity.setPosition(user.getX() + rot.x, user.getEyeY() + rot.y, user.getZ() + rot.z);
                world.spawnEntity(fireballEntity);
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.ghast.fireball"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.blaze_powder")));
        tooltip.add(Text.translatable("tooltip.mob"));
    }
}