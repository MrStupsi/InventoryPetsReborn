package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EndermanPet extends InventoryPet {
    public static EndermanPet INSTANCE = new EndermanPet();

    public EndermanPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(20),
                4, 8,
                ObsidianNugget.INSTANCE
        );
        MinecraftForge.EVENT_BUS.register(this);
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
            if (!world.isClientSide) teleport(user);
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.enderman.teleport"));
        tooltip.add(Component.translatable("tooltip.enderman.autoteleport"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.obsidian_nugget")));
        tooltip.add(Component.translatable("tooltip.mob"));
    }

    private boolean teleport(Player p) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        Random r = new Random();
        Level w = p.level;
        for (int i = 0; i < 16; ++i) {
            double x2 = p.getX() + (r.nextDouble() - 0.5) * 16.0;
            double y2 = Math.min(
                    Math.max(p.getY() + (double) (r.nextInt(16) - 8), (double) w.getMinBuildHeight()),
                    (double) (w.getMinBuildHeight() + ((ServerLevel) w).getLogicalHeight() - 1)
            );
            double z2 = p.getZ() + (r.nextDouble() - 0.5) * 16.0;
            if (p.isVehicle()) p.stopRiding();
            Vec3 vec3d = p.position();
            p.teleportTo(x2, y2, z2);
            w.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            p.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getHealth() - e.getAmount() <= p.getMaxHealth() * 0.25) {
                int activePet = InventoryPet.getActivePet(p, this);
                if (activePet != -1) {
                    if (!p.isCreative() && !p.isSpectator()) {
                        ItemStack is = p.getInventory().getItem(activePet);
                        is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 2, 0), is.getMaxDamage()));
                        p.getInventory().setItem(activePet, is);
                    }
                    teleport(p);
                }
            }
        }
    }
}