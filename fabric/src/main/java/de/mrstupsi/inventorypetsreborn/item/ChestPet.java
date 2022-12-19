package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ChestPet extends InventoryPet {
    public static ChestPet INSTANCE;

    public ChestPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(15),
                3, 6,
                Items.         ACACIA_LOG, Items.         BIRCH_LOG, Items.         DARK_OAK_LOG, Items.         JUNGLE_LOG,
                Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_JUNGLE_LOG,
                Items.         MANGROVE_LOG, Items.         OAK_LOG, Items.         SPRUCE_LOG, Items.         CRIMSON_STEM,
                Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_OAK_LOG, Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_CRIMSON_STEM,
                Items.         WARPED_STEM,
                Items.STRIPPED_WARPED_STEM
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack is = hand == Hand.MAIN_HAND ? user.getMainHandStack() : user.getOffHandStack();
        if (InventoryPet.isActive(is)) {
            if (!user.isCreative() && !user.isSpectator()) {
                is.setDamage(MathHelper.clamp(is.getDamage() + 1, 0, getMaxDamage()));
                if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is);
                else user.getInventory().offHand.set(0, is);
            }
            if (!world.isClient) {
                DefaultedList<ItemStack> items = DefaultedList.ofSize(9 * 3, ItemStack.EMPTY);
                NbtCompound nbt = is.getOrCreateNbt();
                if (!nbt.contains("chest_pet")) {
                    nbt.put("chest_pet", new NbtCompound());
                }
                NbtCompound chest_pet = nbt.getCompound("chest_pet");
                if (!chest_pet.contains("id")) {
                    chest_pet.putString("id", UUID.randomUUID().toString());
                }
                String id = chest_pet.getString("id");
                if (chest_pet.contains("content")) Inventories.readNbt(chest_pet.getCompound("content"), items);
                ItemStack[] itemArray = new ItemStack[items.size()];
                for (int i = 0; i < items.size(); i++) itemArray[i] = items.get(i);
                SimpleInventory inv = new SimpleInventory(itemArray);
                inv.addListener(inventory -> {
                    ItemStack is2 = hand == Hand.MAIN_HAND ? user.getMainHandStack() : user.getOffHandStack();
                    NbtCompound nbt2 = is2.getOrCreateNbt();
                    if (!nbt2.contains("chest_pet")) return;
                    NbtCompound chest_pet2 = nbt2.getCompound("chest_pet");
                    if (!chest_pet2.contains("id")) return;
                    String id2 = chest_pet2.getString("id");
                    if (id.equals(id2)) {
                        if (!chest_pet2.contains("content")) chest_pet2.put("content", new NbtCompound());
                        NbtCompound content = chest_pet2.getCompound("content");
                        Inventories.writeNbt(content, inv.stacks);
                        chest_pet2.put("content", content);
                        nbt2.put("chest_pet", chest_pet2);
                        is2.setNbt(nbt2);
                        if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is2);
                        else user.getInventory().offHand.set(0, is2);
                    }
                });
                user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
                    return GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, inv);
                }, Text.translatable("item.inventorypetsreborn.chest_pet")));
                user.incrementStat(Stats.OPEN_CHEST);
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.chest.open"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.logs")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}