package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FurnacePet extends InventoryPet {
    public static FurnacePet INSTANCE = new FurnacePet();

    public FurnacePet() {
        super(
                new Properties().rarity(Rarity.COMMON).durability(40),
                15, 20,
                Items.COAL
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack is = hand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
        if (InventoryPet.isActive(is)) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemToSmelt = user.getInventory().getItem(i);
                if (itemToSmelt != null) {
                    Optional<SmeltingRecipe> recipe = getRecipe(world, itemToSmelt);
                    if (recipe.isPresent()) {
                        if (!user.isCreative() && !user.isSpectator()) {
                            is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                            if (hand == InteractionHand.MAIN_HAND) user.getInventory().setItem(user.getInventory().selected, is);
                            else user.getInventory().offhand.set(0, is);
                        }
                        itemToSmelt.setCount(itemToSmelt.getCount() - 1);
                        user.getInventory().setItem(i, itemToSmelt);
                        user.getInventory().add(recipe.get().getResultItem().copy());
                        user.awardStat(Stats.INTERACT_WITH_FURNACE);
                        return InteractionResultHolder.success(is);
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.furnace.smelt"));
        tooltip.add(Component.translatable("tooltip.favoritefood").append(Component.translatable("tooltip.coal")));
        tooltip.add(Component.translatable("tooltip.neutral"));
    }

    private Optional<SmeltingRecipe> getRecipe(Level w, ItemStack is) {
        return w.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(is), w);
    }
}