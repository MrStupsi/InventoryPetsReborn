package de.mrstupsi.inventorypetsreborn.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantingTablePet extends InventoryPet {
    public static EnchantingTablePet INSTANCE;

    public EnchantingTablePet() {
        super(
                new Settings().rarity(Rarity.COMMON).maxDamage(10),
                4, 6,
                LapisNugget.INSTANCE
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
                    EnchantmentScreenHandler handler = new EnchantmentScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, new BlockPos(0, 0, 0))) {
                        Random r = Random.create();

                        @Override
                        public boolean canUse(PlayerEntity player) {
                            return true;
                        }

                        private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
                            List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(r, stack, level, false);
                            if (stack.isOf(Items.BOOK) && list.size() > 1) {
                                list.remove(r.nextInt(list.size()));
                            }
                            return list;
                        }

                        public void onContentChanged(Inventory inventory) {
                            ItemStack itemStack = inventory.getStack(0);
                            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                                int i = r.nextInt(16);
                                int j;
                                for(j = 0; j < 3; ++j) {
                                    this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(r, j, i, itemStack);
                                    this.enchantmentId[j] = -1;
                                    this.enchantmentLevel[j] = -1;
                                    if (this.enchantmentPower[j] < j + 1) {
                                        this.enchantmentPower[j] = 0;
                                    }
                                }
                                for (j = 0; j < 3; ++j) {
                                    if (this.enchantmentPower[j] > 0) {
                                        List<EnchantmentLevelEntry> list = generateEnchantments(itemStack, j, this.enchantmentPower[j]);
                                        if (list != null && !list.isEmpty()) {
                                            EnchantmentLevelEntry enchantmentLevelEntry = list.get(r.nextInt(list.size()));
                                            this.enchantmentId[j] = Registry.ENCHANTMENT.getRawId(enchantmentLevelEntry.enchantment);
                                            this.enchantmentLevel[j] = enchantmentLevelEntry.level;
                                        }
                                    }
                                }
                                this.sendContentUpdates();
                            }
                        }
                    };
                    return handler;
                }, Text.translatable("container.enchanting")));
            }
            return TypedActionResult.success(is);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.enchantingtable.open"));
        tooltip.add(Text.translatable("tooltip.favoritefood").append(Text.translatable("tooltip.lapis_nugget")));
        tooltip.add(Text.translatable("tooltip.neutral"));
    }
}