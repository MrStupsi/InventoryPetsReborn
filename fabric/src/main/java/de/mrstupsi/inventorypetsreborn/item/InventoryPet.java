package de.mrstupsi.inventorypetsreborn.item;

import com.google.common.collect.Lists;
import de.mrstupsi.inventorypetsreborn.Tick;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class InventoryPet extends Item {
    public static ItemGroup INVENTORY_PETS_GROUP = FabricItemGroupBuilder.create(new Identifier("inventorypetsreborn", "inventory_pets")).icon(() -> new ItemStack(SpiderPet.INSTANCE)).build();
    private final int minDamageToRepair;
    private final int repair;
    private final List<Item> foods;

    public InventoryPet(Settings settings, int minDamageToRepair, int repair, Item... foods) {
        super(settings.group(INVENTORY_PETS_GROUP));
        this.minDamageToRepair = minDamageToRepair;
        this.repair = repair;
        this.foods = Lists.newArrayList(foods);
    }

    public InventoryPet(Settings settings) {
        this(settings, 0, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!foods.isEmpty() && stack.getDamage() >= minDamageToRepair &&
                Tick.TICK % (20 * 5) == 0 &&
                entity instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) entity;
            for (int i = 0; i < 9; i++) {
                ItemStack is = p.getInventory().getStack(i);
                if (is != null && !is.isEmpty() && foods.contains(is.getItem())) {
                    is.setCount(is.getCount() - 1);
                    stack.setDamage(MathHelper.clamp(stack.getDamage() - repair, 0, getMaxDamage()));
                    p.getInventory().setStack(i, is);
                    p.getInventory().setStack(slot, stack);
                    break;
                }
            }
        }
    }

    public static boolean isActive(ItemStack is) {
        return is.getItem() instanceof InventoryPet && (is.getItem().getMaxDamage() == 0 || is.getDamage() < is.getItem().getMaxDamage());
    }

    public static boolean isActive(ItemStack is, int slot) {
        return slot < 9 && isActive(is);
    }

    public static boolean hasActivePet(Object entity, InventoryPet pet) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            for (int i = 0; i < 9; i++) {
                ItemStack is = player.getInventory().getStack(i);
                if (is != null && is.getItem() == pet && isActive(is)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getActivePet(Object entity, InventoryPet pet) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            for (int i = 0; i < 9; i++) {
                ItemStack is = player.getInventory().getStack(i);
                if (is != null && is.getItem() == pet && isActive(is)) {
                    return i;
                }
            }
        }
        return -1;
    }
}