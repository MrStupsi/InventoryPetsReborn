package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.block.CraftingTableBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CraftingTablePet extends InventoryPet {
    public static CraftingTablePet INSTANCE;

    public CraftingTablePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(20),
                4, 8,
                Items.ACACIA_PLANKS, Items.BIRCH_PLANKS, Items.DARK_OAK_PLANKS, Items.JUNGLE_PLANKS,
                Items.MANGROVE_PLANKS, Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.CRIMSON_PLANKS,
                Items.WARPED_PLANKS
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
                    return new CraftingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, new BlockPos(0, 0, 0))) {
                        @Override
                        public boolean canUse(PlayerEntity player) {
                            return true;
                        }
                    };
                }, Text.translatable("container.crafting")));
                user.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.craftingtable.open"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.planks")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}