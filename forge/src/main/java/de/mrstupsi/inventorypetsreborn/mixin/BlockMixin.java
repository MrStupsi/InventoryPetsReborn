package de.mrstupsi.inventorypetsreborn.mixin;

import de.mrstupsi.inventorypetsreborn.item.InventoryPet;
import de.mrstupsi.inventorypetsreborn.item.LootPet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collections;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static List<ItemStack> getDrops(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack) {
        LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, stack).withOptionalParameter(LootContextParams.THIS_ENTITY, entity).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
        List<ItemStack> stacks = state.getDrops(builder);
        int activePet = InventoryPet.getActivePet(entity, LootPet.INSTANCE);
        if ((state.getBlock() == Blocks.COAL_ORE || state.getBlock() == Blocks.DEEPSLATE_COAL_ORE ||
             state.getBlock() == Blocks.COPPER_ORE || state.getBlock() == Blocks.DEEPSLATE_COPPER_ORE ||
             state.getBlock() == Blocks.DIAMOND_ORE || state.getBlock() == Blocks.DEEPSLATE_DIAMOND_ORE ||
             state.getBlock() == Blocks.EMERALD_ORE || state.getBlock() == Blocks.DEEPSLATE_EMERALD_ORE ||
             state.getBlock() == Blocks.GOLD_ORE || state.getBlock() == Blocks.DEEPSLATE_GOLD_ORE ||
             state.getBlock() == Blocks.IRON_ORE || state.getBlock() == Blocks.DEEPSLATE_IRON_ORE ||
             state.getBlock() == Blocks.LAPIS_ORE || state.getBlock() == Blocks.DEEPSLATE_LAPIS_ORE ||
             state.getBlock() == Blocks.REDSTONE_ORE || state.getBlock() == Blocks.DEEPSLATE_REDSTONE_ORE ||
             state.getBlock() == Blocks.NETHER_GOLD_ORE || state.getBlock() == Blocks.NETHER_QUARTZ_ORE) &&
             activePet != -1) {
            Player p = (Player) entity;
            if (!p.isCreative() && !p.isSpectator()) {
                ItemStack is = p.getInventory().getItem(activePet);
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                p.getInventory().setItem(activePet, is);
            }
            stacks.forEach(is -> is.setCount(is.getCount() * 2));
        }
        return stacks;
    }
}