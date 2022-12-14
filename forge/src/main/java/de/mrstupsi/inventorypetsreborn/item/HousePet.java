package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HousePet extends InventoryPet {
    public static HousePet INSTANCE = new HousePet();

    public HousePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(20),
                4, 8,
                PetType.NEUTRAL,
                EnderNugget.INSTANCE
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
                ServerPlayer sp = (ServerPlayer) user;
                if (user.isCrouching()) {
                    sp.setRespawnPosition(sp.level.dimension(), sp.blockPosition(), sp.getRespawnAngle(), false, true);
                } else if (sp.getRespawnPosition() != null) {
                    BlockPos pos = sp.getRespawnPosition();
                    double x = user.getX();
                    double y = user.getY();
                    double z = user.getZ();
                    sp.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    world.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    sp.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.house.teleport"));
        tooltip.add(Component.translatable("tooltip.house.setspawn"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.ender_nugget")));
        tooltip.add(getType().getTooltip());
    }
}