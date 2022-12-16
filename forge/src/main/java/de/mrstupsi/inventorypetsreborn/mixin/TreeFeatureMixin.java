package de.mrstupsi.inventorypetsreborn.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.BiConsumer;

@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {
    private static final Random r = new Random();

    @Shadow protected abstract int getMaxFreeTreeHeight(LevelSimulatedReader p_67216_, int p_67217_, BlockPos p_67218_, TreeConfiguration p_67219_);

    @Inject(at = @At("HEAD"), method = "doPlace", cancellable = true)
    private void doPlace(WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> p_225261_, BiConsumer<BlockPos, BlockState> p_225262_, BiConsumer<BlockPos, BlockState> p_225263_, TreeConfiguration config, CallbackInfoReturnable<Boolean> cir) {
        if (config.trunkPlacer instanceof StraightTrunkPlacer) {
            if (r.nextInt(50) == 0) cir.cancel();
            else return;
        } else return;
        int i = config.trunkPlacer.getTreeHeight(rand);
        int j = config.foliagePlacer.foliageHeight(rand, i, config);
        int k = i - j;
        int l = config.foliagePlacer.foliageRadius(rand, k);
        BlockPos blockpos = config.rootPlacer.map((p_225286_) -> p_225286_.getTrunkOrigin(pos, rand)).orElse(pos);
        int i1 = Math.min(pos.getY(), blockpos.getY());
        int j1 = Math.max(pos.getY(), blockpos.getY()) + i + 1;
        if (i1 >= world.getMinBuildHeight() + 1 && j1 <= world.getMaxBuildHeight()) {
            OptionalInt optionalint = config.minimumSize.minClippedHeight();
            int k1 = this.getMaxFreeTreeHeight(world, i, blockpos, config);
            if (k1 >= i || !optionalint.isEmpty() && k1 >= optionalint.getAsInt()) {
                if (config.rootPlacer.isPresent() && !config.rootPlacer.get().placeRoots(world, p_225261_, rand, pos, blockpos, config)) {
                    cir.setReturnValue(false);
                } else {
                    List<FoliagePlacer.FoliageAttachment> list = config.trunkPlacer.placeTrunk(world, p_225262_, rand, k1, blockpos, config);
                    list.forEach((p_225279_) -> {
                        config.foliagePlacer.createFoliage(world, p_225263_, rand, config, k1, p_225279_, j, l);
                    });
                    world.setBlock(blockpos.above(k1 - 1), Blocks.CHEST.defaultBlockState(), 19);
                    ChestBlockEntity cbe = (ChestBlockEntity) world.getBlockEntity(blockpos.above(k1 - 1));
                    cbe.setLootTable(new ResourceLocation("inventorypetsreborn", "chests/tree_top"), r.nextInt());
                    cir.setReturnValue(true);
                }
            } else {
                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(false);
        }
    }
}