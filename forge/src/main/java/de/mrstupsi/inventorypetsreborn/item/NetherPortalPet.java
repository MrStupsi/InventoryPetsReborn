package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NetherPortalPet extends InventoryPet {
    public static NetherPortalPet INSTANCE = new NetherPortalPet();

    public NetherPortalPet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(10),
                3, 4,
                PetType.NEUTRAL,
                Items.QUARTZ
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack is = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
        if (InventoryPet.isActive(is)) {
            if (!user.isCreative() && !user.isSpectator()) {
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 2, 0), is.getMaxDamage()));
                if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                else user.getInventory().offhand.set(0, is);
            }
            if (!world.isClientSide) {
                MinecraftServer server = world.getServer();
                ResourceKey<Level> registryKey = world.dimension() == Level.NETHER ? Level.OVERWORLD : Level.NETHER;
                ServerLevel world2 = server.getLevel(registryKey);
                if (world2 != null && server.isNetherEnabled() && !user.isPassenger()) {
                    world.getProfiler().push("portal");
                    user.setPortalCooldown();
                    user.changeDimension(world2);
                    world.getProfiler().pop();
                }
            }
            return InteractionResultHolder.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.netherportal.switchdimension"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.quartz")));
        tooltip.add(getType().getTooltip());
    }
}