package de.mrstupsi.inventorypetsreborn.item;

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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IronGolemPet extends InventoryPet {
    public static IronGolemPet INSTANCE = new IronGolemPet();

    public IronGolemPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(20),
                4, 8,
                PetType.FRIENDLY,
                Items.IRON_INGOT
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
            user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 4, false, false, false));
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.irongolem.absorbtion"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.iron_ingot")));
        tooltip.add(getType().getTooltip());
    }
}