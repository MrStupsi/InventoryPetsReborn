package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EndermanPet extends InventoryPet {
    public static EndermanPet INSTANCE;

    public EndermanPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(20),
                4, 8,
                ObsidianNugget.INSTANCE
        );
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity) {
                PlayerEntity p = (PlayerEntity) entity;
                if (p.getHealth() - amount <= p.getMaxHealth() * 0.25) {
                    int activePet = InventoryPet.getActivePet(p, this);
                    if (activePet != -1) {
                        if (!p.isCreative() && !p.isSpectator()) {
                            ItemStack is = p.getInventory().getStack(activePet);
                            is.setDamage(MathHelper.clamp(is.getDamage() + 2, 0, getMaxDamage()));
                            p.getInventory().setStack(activePet, is);
                        }
                        teleport(p);
                    }
                }
            }
            return true;
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
            if (!world.isClient) teleport(user);
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.enderman.teleport"));
        tooltip.add(Text.translatable("tooltip.enderman.autoteleport"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.obsidian_nugget")));
        tooltip.add(Text.translatable("tooltip.mob"));
    }

    private boolean teleport(PlayerEntity p) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        Random r = new Random();
        World w = p.world;
        for (int i = 0; i < 16; ++i) {
            double x2 = p.getX() + (r.nextDouble() - 0.5) * 16.0;
            double y2 = MathHelper.clamp(p.getY() + (double) (r.nextInt(16) - 8), (double) w.getBottomY(), (double) (w.getBottomY() + ((ServerWorld) w).getLogicalHeight() - 1));
            double z2 = p.getZ() + (r.nextDouble() - 0.5) * 16.0;
            if (p.hasVehicle()) p.stopRiding();
            Vec3d vec3d = p.getPos();
            if (p.teleport(x2, y2, z2, true)) {
                w.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(p));
                w.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                p.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }
}