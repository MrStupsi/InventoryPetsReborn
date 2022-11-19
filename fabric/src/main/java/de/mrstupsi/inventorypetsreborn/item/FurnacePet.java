package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FurnacePet extends InventoryPet {
    public static FurnacePet INSTANCE;

    public FurnacePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(40),
                15, 20,
                Items.COAL
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack is = hand == Hand.MAIN_HAND ? user.getMainHandStack() : user.getOffHandStack();
        if (InventoryPet.isActive(is)) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemToSmelt = user.getInventory().getStack(i);
                if (itemToSmelt != null) {
                    Optional<SmeltingRecipe> recipe = getRecipe(world, itemToSmelt);
                    if (recipe.isPresent()) {
                        if (!user.isCreative() && !user.isSpectator()) {
                            is.setDamage(MathHelper.clamp(is.getDamage() + 1, 0, getMaxDamage()));
                            if (hand == Hand.MAIN_HAND) user.getInventory().main.set(user.getInventory().selectedSlot, is);
                            else user.getInventory().offHand.set(0, is);
                        }
                        itemToSmelt.setCount(itemToSmelt.getCount() - 1);
                        user.getInventory().setStack(i, itemToSmelt);
                        user.giveItemStack(recipe.get().getOutput().copy());
                        user.incrementStat(Stats.INTERACT_WITH_FURNACE);
                        return TypedActionResult.success(is);
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.furnace.smelt"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.coal")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }

    private Optional<SmeltingRecipe> getRecipe(World w, ItemStack is) {
        return w.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(is), w);
    }
}