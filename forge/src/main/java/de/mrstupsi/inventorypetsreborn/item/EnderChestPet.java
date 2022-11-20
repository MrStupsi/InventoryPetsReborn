package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderChestPet extends InventoryPet {
    public static EnderChestPet INSTANCE = new EnderChestPet();

    public EnderChestPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(15),
                3, 6,
                EnderNugget.INSTANCE
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack is = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
        if (InventoryPet.isActive(is)) {
            if (!user.isCreative() && !user.isSpectator()) {
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                else user.getInventory().offhand.set(0, is);
            }
            if (!world.isClientSide) {
                user.openMenu(new SimpleMenuProvider((syncId, inventory, player) -> {
                    return ChestMenu.threeRows(syncId, inventory, user.getEnderChestInventory());
                }, Component.translatable("container.enderchest")));
                user.awardStat(Stats.OPEN_ENDERCHEST);
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.enderchest.open"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.ender_nugget")));
        tooltip.add(Component.translatable("tooltip.neutral"));
    }
}