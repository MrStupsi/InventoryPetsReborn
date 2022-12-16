package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreeperPet extends InventoryPet {
    public static CreeperPet INSTANCE = new CreeperPet();

    public CreeperPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(20),
                4, 8,
                PetType.MOB,
                Items.GUNPOWDER
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
            user.level.explode(user, user.getX(), user.getY(), user.getZ(), 3.0F, Explosion.BlockInteraction.BREAK);
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.creeper.explode"));
        tooltip.add(Component.translatable("tooltip.creeper.explosionresistance"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.gunpowder")));
        tooltip.add(getType().getTooltip());
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent e) {
        if (e.getSource().isExplosion() && e.getEntity() instanceof Player) {
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