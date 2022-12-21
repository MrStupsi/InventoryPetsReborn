package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DoubleChestPet extends InventoryPet {
    public static DoubleChestPet INSTANCE = new DoubleChestPet();

    public DoubleChestPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(15),
                3, 6,
                PetType.NEUTRAL,
                Items.         ACACIA_LOG, Items.         BIRCH_LOG, Items.         DARK_OAK_LOG, Items.         JUNGLE_LOG,
                Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_JUNGLE_LOG,
                Items.         MANGROVE_LOG, Items.         OAK_LOG, Items.         SPRUCE_LOG, Items.         CRIMSON_STEM,
                Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_OAK_LOG, Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_CRIMSON_STEM,
                Items.         WARPED_STEM,
                Items.STRIPPED_WARPED_STEM
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
                CompoundTag nbt = is.getOrCreateTag();
                if (!nbt.contains("double_chest_pet")) {
                    nbt.put("double_chest_pet", new CompoundTag());
                }
                CompoundTag double_chest_pet = nbt.getCompound("double_chest_pet");
                if (!double_chest_pet.contains("id")) {
                    double_chest_pet.putString("id", UUID.randomUUID().toString());
                }
                String id = double_chest_pet.getString("id");
                user.openMenu(new SimpleMenuProvider((syncId, inventory, player) -> {
                    ChestMenu inv = ChestMenu.threeRows(syncId, inventory);
                    if (!double_chest_pet.contains("content")) double_chest_pet.put("content", new CompoundTag());
                    ListTag content = double_chest_pet.getList("content", ListTag.TAG_COMPOUND);
                    for (int i = 0; i < 3 * 9; i++) {
                        CompoundTag item = content.getCompound(i);
                        if (item.contains("id")) {
                            ItemStack stack = new ItemStack(Item.byId(item.getInt("id")));
                            stack.setDamageValue(item.getInt("damage"));
                            stack.setCount(item.getInt("count"));
                            stack.setTag(item.getCompound("tag"));
                        } else inv.setRemoteSlot(i, ItemStack.EMPTY);
                    }
                    inv.addSlotListener(new ContainerListener() {
                        @Override
                        public void slotChanged(AbstractContainerMenu p_39315_, int p_39316_, ItemStack p_39317_) {
                            ItemStack is2 = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
                            CompoundTag nbt2 = is2.getOrCreateTag();
                            if (!nbt2.contains("double_chest_pet")) return;
                            CompoundTag double_chest_pet2 = nbt2.getCompound("double_chest_pet");
                            if (!double_chest_pet2.contains("id")) return;
                            String id2 = double_chest_pet2.getString("id");
                            if (id.equals(id2)) {
                                ListTag content = new ListTag();
                                for (int i = 0; i < 3 * 9; i++) {
                                    CompoundTag item = new CompoundTag();
                                    ItemStack stack = inv.getSlot(i).getItem();
                                    item.putInt("id", Item.getId(stack.getItem()));
                                    item.putInt("damage", stack.getDamageValue());
                                    item.putInt("count", stack.getCount());
                                    item.put("tag", stack.getOrCreateTag());
                                    content.add(item);
                                }
                                double_chest_pet2.put("content", content);
                                nbt2.put("double_chest_pet", double_chest_pet2);
                                is2.setTag(nbt2);
                                if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is2);
                                else user.getInventory().offhand.set(0, is2);
                            }
                        }

                        @Override
                        public void dataChanged(AbstractContainerMenu p_150524_, int p_150525_, int p_150526_) {}
                    });
                    return inv;
                }, Component.translatable("item.inventorypetsreborn.doublechest_pet")));
                user.awardStat(Stats.OPEN_CHEST);
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.doublechest.open"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.logs")));
        tooltip.add(getType().getTooltip());
    }
}