package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IlluminatiPet extends InventoryPet {
    public static IlluminatiPet INSTANCE = new IlluminatiPet();

    public IlluminatiPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(3),
                2, 3,
                EmeraldNugget.INSTANCE
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack is = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
        if (InventoryPet.isActive(is)) {
            CompoundTag nbt = is.getTag();
            if (!nbt.contains("illuminati_pet")) nbt.put("illuminati_pet", new CompoundTag());
            CompoundTag illuminati_pet = nbt.getCompound("illuminati_pet");
            if (!illuminati_pet.contains("last_item")) illuminati_pet.putLong("last_item", 0);
            long last_item = illuminati_pet.getLong("last_item");
            if (last_item + 1000 * 60 * 3 <= System.currentTimeMillis() ||
                    user.isCreative()) {
                if (!user.isCreative() && !user.isSpectator()) {
                    is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                    if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                    else user.getInventory().offhand.set(0, is);
                }
                if (world.isClientSide) {
                    user.playSound(new SoundEvent(new ResourceLocation("inventorypetsreborn", "illuminati")), 1.0F, 1.0F);
                } else {
                    Item item = Registry.ITEM.getRandom(world.random).get().value();
                    user.getInventory().add(new ItemStack(item));
                }
                illuminati_pet.putLong("last_item", System.currentTimeMillis());
                illuminati_pet.putBoolean("notified", user.isCreative());
                nbt.put("illuminati_pet", illuminati_pet);
                is.setTag(nbt);
                if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                else user.getInventory().offhand.set(0, is);
                return InteractionResultHolder.success(is);
            } else if (!world.isClientSide) {
                user.sendSystemMessage(Component.translatable("illuminati_pet.cooldown"));
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player) {
            Player p = (Player) entity;
            CompoundTag nbt = stack.getTag();
            if (nbt.contains("illuminati_pet")) {
                CompoundTag illuminati_pet = nbt.getCompound("illuminati_pet");
                if (illuminati_pet.contains("last_item")) {
                    long last_item = illuminati_pet.getLong("last_item");
                    if (!illuminati_pet.contains("notified")) illuminati_pet.putBoolean("notified", false);
                    boolean notified = illuminati_pet.getBoolean("notified");
                    if (last_item + 1000 * 60 * 3 <= System.currentTimeMillis() &&
                            !notified) {
                        entity.sendSystemMessage(Component.translatable("illuminati_pet.ready"));
                        illuminati_pet.putBoolean("notified", true);
                        nbt.put("illuminati_pet", illuminati_pet);
                        stack.setTag(nbt);
                        p.getInventory().setItem(slot, stack);
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.illuminati.spawnitem"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.emerald_nugget")));
        tooltip.add(Component.translatable("tooltip.legendary"));
    }
}