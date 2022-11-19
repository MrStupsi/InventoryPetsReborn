package de.mrstupsi.inventorypetsreborn.item;

import net.fabricmc.fabric.api.screenhandler.v1.FabricScreenHandlerFactory;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderChestPet extends InventoryPet {
    public static EnderChestPet INSTANCE;

    public EnderChestPet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(15),
                3, 6,
                EnderNugget.INSTANCE
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
                user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
                    return GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, user.getEnderChestInventory());
                }, Text.translatable("container.enderchest")));
                user.incrementStat(Stats.OPEN_ENDERCHEST);
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.enderchest.open"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.ender_nugget")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}