package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.TooltipFlag;
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

public class CraftingTablePet extends InventoryPet {
    public static CraftingTablePet INSTANCE = new CraftingTablePet();

    public CraftingTablePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(20),
                4, 8,
                Items.ACACIA_PLANKS, Items.BIRCH_PLANKS, Items.DARK_OAK_PLANKS, Items.JUNGLE_PLANKS,
                Items.MANGROVE_PLANKS, Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.CRIMSON_PLANKS,
                Items.WARPED_PLANKS
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
                    return new CraftingMenu(syncId, inventory, ContainerLevelAccess.create(world, user.getOnPos())) {
                        @Override
                        public boolean stillValid(Player player) {
                            return true;
                        }
                    };
                }, Component.translatable("container.crafting")));
                user.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.craftingtable.open"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.planks")));
        tooltip.add(Component.translatable("tooltip.neutral"));
    }
}