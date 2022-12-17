package de.mrstupsi.inventorypetsreborn.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.root.RootPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
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
    @Shadow protected abstract int getTopPosition(TestableWorld world, int height, BlockPos pos, TreeFeatureConfig config);

    private static final Random r = new Random();

    @Inject(at = @At("HEAD"), method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;Ljava/util/function/BiConsumer;Ljava/util/function/BiConsumer;Ljava/util/function/BiConsumer;Lnet/minecraft/world/gen/feature/TreeFeatureConfig;)Z", cancellable = true)
    private void doPlace(StructureWorldAccess world, net.minecraft.util.math.random.Random rand, BlockPos pos, BiConsumer<BlockPos, BlockState> rootPlacerReplacer, BiConsumer<BlockPos, BlockState> trunkPlacerReplacer, BiConsumer<BlockPos, BlockState> foliagePlacerReplacer, TreeFeatureConfig config, CallbackInfoReturnable<Boolean> cir) {
        if (config.trunkPlacer instanceof StraightTrunkPlacer) {
            if (r.nextInt(50) == 0) cir.cancel();
            else return;
        } else return;
        int i = config.trunkPlacer.getHeight(rand);
        int j = config.foliagePlacer.getRandomHeight(rand, i, config);
        int k = i - j;
        int l = config.foliagePlacer.getRandomRadius(rand, k);
        BlockPos blockPos = config.rootPlacer.map((rootPlacer) -> rootPlacer.trunkOffset(pos, rand)).orElse(pos);
        int m = Math.min(pos.getY(), blockPos.getY());
        int n = Math.max(pos.getY(), blockPos.getY()) + i + 1;
        if (m >= world.getBottomY() + 1 && n <= world.getTopY()) {
            OptionalInt optionalInt = config.minimumSize.getMinClippedHeight();
            int o = this.getTopPosition(world, i, blockPos, config);
            if (o < i && (optionalInt.isEmpty() || o < optionalInt.getAsInt())) {
                cir.setReturnValue(false);
            } else if (config.rootPlacer.isPresent() && !((RootPlacer)config.rootPlacer.get()).generate(world, rootPlacerReplacer, rand, pos, blockPos, config)) {
                cir.setReturnValue(false);
            } else {
                List<FoliagePlacer.TreeNode> list = config.trunkPlacer.generate(world, trunkPlacerReplacer, rand, o, blockPos, config);
                list.forEach((node) -> {
                    config.foliagePlacer.generate(world, foliagePlacerReplacer, rand, config, o, node, j, l);
                });
                world.setBlockState(blockPos.up(o - 1), Blocks.CHEST.getDefaultState(), 19);
                ChestBlockEntity cbe = (ChestBlockEntity) world.getBlockEntity(blockPos.up(o - 1));
                cbe.setLootTable(new Identifier("inventorypetsreborn", "chests/tree_top"), r.nextInt());
                cir.setReturnValue(true);
            }
        } else {
            cir.setReturnValue(false);
        }
    }
}