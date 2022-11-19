package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SilverfishPet extends InventoryPet {
    public static SilverfishPet INSTANCE;

    public SilverfishPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(200),
                40, 80,
                Items.GUNPOWDER
        );
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (source == DamageSource.IN_WALL && entity instanceof PlayerEntity) {
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack is = hand == Hand.MAIN_HAND ? user.getMainHandStack() : user.getOffHandStack();
        if (InventoryPet.isActive(is)) {
            Vec3d pos = user.getPos().add(user.getRotationVector());
            BlockState state = world.getBlockState(new BlockPos(pos));
            BlockState state2 = world.getBlockState(new BlockPos(pos.add(0, user.getEyeHeight(user.getPose()), 0)));
            BlockState stateHead = world.getBlockState(new BlockPos(pos.add(user.getRotationVector())));
            if (!state2.isAir() || !stateHead.isAir()) {
                if (!stateHead.isAir()) {
                    state = world.getBlockState(new BlockPos(pos.add(user.getRotationVector())));
                    state2 = world.getBlockState(new BlockPos(pos.add(user.getRotationVector()).add(0, user.getEyeHeight(user.getPose()), 0)));
                }
                for (int i = 0; i < 10 && !(state.isAir() && state2.isAir()); i++) {
                    System.out.println(state + " " + state2);
                    pos = pos.add(user.getRotationVector());
                    state = world.getBlockState(new BlockPos(pos));
                    state2 = world.getBlockState(new BlockPos(pos.add(0, user.getEyeHeight(user.getPose()), 0)));
                }
                if (state.isAir() && state2.isAir()) {
                    if (!user.isCreative() && !user.isSpectator()) {
                        is.setDamage(MathHelper.clamp(is.getDamage() + 50, 0, getMaxDamage()));
                        if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is);
                        else user.getInventory().offHand.set(0, is);
                    }
                    user.teleport(pos.x, pos.y, pos.z);
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20));
                    return TypedActionResult.success(is);
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.silverfish.suffocationimmunity"));
        tooltip.add(Text.translatable("tooltip.silverfish.walkthroughwall"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.cobblestone")));
        tooltip.add(Text.translatable("tooltip.mob"));
    }
}