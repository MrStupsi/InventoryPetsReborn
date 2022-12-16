package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SilverfishPet extends InventoryPet {
    public static SilverfishPet INSTANCE = new SilverfishPet();

    public SilverfishPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(200),
                40, 80,
                PetType.MOB,
                Items.COBBLESTONE
        );
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack is = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
        if (InventoryPet.isActive(is)) {
            Vec3 pos = user.position().add(user.getLookAngle());
            BlockState state = world.getBlockState(new BlockPos(pos));
            BlockState state2 = world.getBlockState(new BlockPos(pos.add(0, user.getEyeHeight(user.getPose()), 0)));
            BlockState stateHead = world.getBlockState(new BlockPos(pos.add(user.getLookAngle())));
            if (!state2.isAir() || !stateHead.isAir()) {
                if (!stateHead.isAir()) {
                    state = world.getBlockState(new BlockPos(pos.add(user.getLookAngle())));
                    state2 = world.getBlockState(new BlockPos(pos.add(user.getLookAngle()).add(0, user.getEyeHeight(user.getPose()), 0)));
                }
                for (int i = 0; i < 10 && !(state.isAir() && state2.isAir()); i++) {
                    System.out.println(state + " " + state2);
                    pos = pos.add(user.getLookAngle());
                    state = world.getBlockState(new BlockPos(pos));
                    state2 = world.getBlockState(new BlockPos(pos.add(0, user.getEyeHeight(user.getPose()), 0)));
                }
                if (state.isAir() && state2.isAir()) {
                    if (!user.isCreative() && !user.isSpectator()) {
                        is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 50, 0), is.getMaxDamage()));
                        if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                        else user.getInventory().offhand.set(0, is);
                    }
                    user.teleportTo(pos.x, pos.y, pos.z);
                    user.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20));
                    return InteractionResultHolder.success(is);
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.silverfish.suffocationimmunity"));
        tooltip.add(Component.translatable("tooltip.silverfish.walkthroughwall"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.cobblestone")));
        tooltip.add(getType().getTooltip());
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent e) {
        if (e.getSource() == DamageSource.IN_WALL && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            int activePet = InventoryPet.getActivePet(p, this);
            if (activePet != -1) {
                if (!p.isCreative() && !p.isSpectator()) {
                    ItemStack is = p.getInventory().getItem(activePet);
                    is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                    p.getInventory().setItem(activePet, is);
                }
                e.setCanceled(true);
            }
        }
    }
}