package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantingTablePet extends InventoryPet {
    public static EnchantingTablePet INSTANCE = new EnchantingTablePet();

    public EnchantingTablePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(10),
                4, 6,
                PetType.NEUTRAL,
                LapisNugget.INSTANCE
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
                    EnchantmentMenu handler = new EnchantmentMenu(syncId, inventory, ContainerLevelAccess.create(world, user.getOnPos())) {
                        RandomSource r = RandomSource.create();

                        private List<EnchantmentInstance> getEnchantmentList(ItemStack stack, int slot, int level) {
                            List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(r, stack, level, false);
                            if (stack.is(Items.BOOK) && list.size() > 1) {
                                list.remove(r.nextInt(list.size()));
                            }
                            return list;
                        }

                        public void slotsChanged(Inventory inventory) {
                            ItemStack itemStack = inventory.getItem(0);
                            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                                int i = r.nextInt(16);
                                int j;
                                for(j = 0; j < 3; ++j) {
                                    this.costs[j] = EnchantmentHelper.getEnchantmentCost(r, j, i, itemStack);
                                    this.enchantClue[j] = -1;
                                    this.levelClue[j] = -1;
                                    if (this.costs[j] < j + 1) {
                                        this.costs[j] = 0;
                                    }
                                }
                                for (j = 0; j < 3; ++j) {
                                    if (this.costs[j] > 0) {
                                        List<EnchantmentInstance> list = getEnchantmentList(itemStack, j, this.costs[j]);
                                        if (list != null && !list.isEmpty()) {
                                            EnchantmentInstance enchantmentLevelEntry = list.get(r.nextInt(list.size()));
                                            this.enchantClue[j] = Registry.ENCHANTMENT.getId(enchantmentLevelEntry.enchantment);
                                            this.levelClue[j] = enchantmentLevelEntry.level;
                                        }
                                    }
                                }
                                this.broadcastChanges();
                            }
                        }

                        @Override
                        public boolean stillValid(Player player) {
                            return true;
                        }
                    };
                    return handler;
                }, Component.translatable("container.enchant")));
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.enchantingtable.open"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.lapis_nugget")));
        tooltip.add(getType().getTooltip());
    }
}