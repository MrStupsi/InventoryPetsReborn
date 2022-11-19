package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.block.BedBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HousePet extends InventoryPet {
    public static HousePet INSTANCE;

    public HousePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(20),
                4, 8,
                EnderNugget.INSTANCE
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
                if (user.isSneaking()) {
                    user.setSleepingPosition(user.getBlockPos());
                } else if (user.getSleepingPosition().isPresent()) {
                    BlockPos pos = user.getSleepingPosition().get();
                    double x = user.getX();
                    double y = user.getY();
                    double z = user.getZ();
                    if (user.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false)) {
                        world.emitGameEvent(GameEvent.TELEPORT, user.getPos(), GameEvent.Emitter.of(user));
                        world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        user.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.house.teleport"));
        tooltip.add(Text.translatable("tooltip.house.setspawn"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.ender_nugget")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}