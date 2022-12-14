package de.mrstupsi.inventorypetsreborn.item;

import com.google.common.collect.Lists;
import de.mrstupsi.inventorypetsreborn.Tick;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class InventoryPet extends Item {
    public static CreativeModeTab INVENTORY_PETS_GROUP = new CreativeModeTab("inventory_pets") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SpiderPet.INSTANCE);
        }
    };
    private final int minDamageToRepair;
    private final int repair;
    private final PetType type;
    private final List<Item> foods;

    public InventoryPet(Properties settings, int minDamageToRepair, int repair, PetType type, Item... foods) {
        super(settings.tab(INVENTORY_PETS_GROUP));
        this.minDamageToRepair = minDamageToRepair;
        this.repair = repair;
        this.type = type;
        this.foods = Lists.newArrayList(foods);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && !foods.isEmpty() && stack.getDamageValue() >= minDamageToRepair &&
                Tick.TICK % (20 * 5) == 0 &&
                entity instanceof Player) {
            Player p = (Player) entity;
            for (int i = 0; i < 9; i++) {
                ItemStack is = p.getInventory().getItem(i);
                if (is != null && !is.isEmpty() && foods.contains(is.getItem())) {
                    is.setCount(is.getCount() - 1);
                    stack.setDamageValue(Math.min(Math.max(stack.getDamageValue() - repair, 0), stack.getMaxDamage()));
                    p.getInventory().setItem(i, is);
                    p.getInventory().setItem(slot, stack);
                    break;
                }
            }
        }
    }

    public PetType getType() {
        return type;
    }

    public static boolean isActive(ItemStack is) {
        return is.getItem() instanceof InventoryPet && (is.getMaxDamage() == 0 || is.getDamageValue() < is.getMaxDamage());
    }

    public static boolean isActive(ItemStack is, int slot) {
        return slot < 9 && isActive(is);
    }

    public static boolean hasActivePet(Object entity, InventoryPet pet) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            for (int i = 0; i < 9; i++) {
                ItemStack is = player.getInventory().getItem(i);
                if (is != null && is.getItem() == pet && isActive(is)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getActivePet(Object entity, InventoryPet pet) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            for (int i = 0; i < 9; i++) {
                ItemStack is = player.getInventory().getItem(i);
                if (is != null && is.getItem() == pet && isActive(is)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static enum PetTag {
        OVERWORLD, NETHER, END, COMMON, LEGENDARY;
    }

    public static enum PetType {
        NEUTRAL(Component.translatable("tooltip.friendly")),
        MOB(Component.translatable("tooltip.mob")),
        FRIENDLY(Component.translatable("tooltip.friendly")),
        LEGENDARY(Component.translatable("tooltip.legendary"));

        private Component tooltip;

        PetType(Component tooltip) {
            this.tooltip = tooltip;
        }

        public Component getTooltip() {
            return tooltip;
        }
    }
}