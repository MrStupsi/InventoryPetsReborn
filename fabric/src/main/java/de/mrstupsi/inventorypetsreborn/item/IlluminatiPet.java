package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IlluminatiPet extends InventoryPet {
    public static IlluminatiPet INSTANCE;

    public IlluminatiPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(3),
                2, 3,
                EmeraldNugget.INSTANCE
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack is = hand == Hand.MAIN_HAND ? user.getMainHandStack() : user.getOffHandStack();
        if (InventoryPet.isActive(is)) {
            NbtCompound nbt = is.getOrCreateNbt();
            if (!nbt.contains("illuminati_pet")) nbt.put("illuminati_pet", new NbtCompound());
            NbtCompound illuminati_pet = nbt.getCompound("illuminati_pet");
            if (!illuminati_pet.contains("last_item")) illuminati_pet.putLong("last_item", 0);
            long last_item = illuminati_pet.getLong("last_item");
            if (last_item + 1000 * 60 * 3 <= System.currentTimeMillis() ||
                    user.isCreative()) {
                if (!user.isCreative() && !user.isSpectator()) {
                    is.setDamage(MathHelper.clamp(is.getDamage() + 1, 0, getMaxDamage()));
                    if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is);
                    else user.getInventory().offHand.set(0, is);
                }
                if (world.isClient) {
                    user.playSound(new SoundEvent(new Identifier("inventorypetsreborn", "illuminati")), SoundCategory.NEUTRAL, 1.0F, 1.0F);
                } else {
                    Item item = Registry.ITEM.getRandom(world.random).get().value();
                    user.giveItemStack(new ItemStack(item));
                }
                illuminati_pet.putLong("last_item", System.currentTimeMillis());
                illuminati_pet.putBoolean("notified", user.isCreative());
                nbt.put("illuminati_pet", illuminati_pet);
                is.setNbt(nbt);
                if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is);
                else user.getInventory().offHand.set(0, is);
                return TypedActionResult.success(is);
            } else if (!world.isClient) {
                user.sendMessage(Text.translatable("illuminati_pet.cooldown"));
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) entity;
            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains("illuminati_pet")) {
                NbtCompound illuminati_pet = nbt.getCompound("illuminati_pet");
                if (illuminati_pet.contains("last_item")) {
                    long last_item = illuminati_pet.getLong("last_item");
                    if (!illuminati_pet.contains("notified")) illuminati_pet.putBoolean("notified", false);
                    boolean notified = illuminati_pet.getBoolean("notified");
                    if (last_item + 1000 * 60 * 3 <= System.currentTimeMillis() &&
                            !notified) {
                        entity.sendMessage(Text.translatable("illuminati_pet.ready"));
                        illuminati_pet.putBoolean("notified", true);
                        nbt.put("illuminati_pet", illuminati_pet);
                        stack.setNbt(nbt);
                        p.getInventory().setStack(slot, stack);
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.illuminati.spawnitem"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.emerald_nugget")));
        tooltip.add(Text.translatable("tooltip.legendary"));
    }
}