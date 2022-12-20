package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WolfPet extends InventoryPet {
    public static WolfPet INSTANCE;

    public WolfPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(3),
                1, 2,
                Items.BONE
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
                WolfEntity we = new WolfEntity(EntityType.WOLF, world);
                world.spawnEntity(we);
                Text name = user.getName().copy();
                name.getSiblings().add(Text.literal("'s Dog"));
                we.setCustomName(name);
                we.setOwner(user);
                we.teleport(user.getX(), user.getY(), user.getZ());
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.wolf.spawn_wolf"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.bone")));
        tooltip.add(Text.translatable("tooltip.friendly"));
    }
}