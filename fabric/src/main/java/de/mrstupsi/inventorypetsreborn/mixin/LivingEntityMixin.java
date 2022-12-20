package de.mrstupsi.inventorypetsreborn.mixin;

import de.mrstupsi.inventorypetsreborn.item.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow private Optional<BlockPos> climbingPos;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow protected abstract boolean canEnterTrapdoor(BlockPos pos, BlockState state);

    @Shadow public abstract EntityGroup getGroup();

    @Shadow protected abstract void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity);

    @Shadow public abstract SoundEvent getEatSound(ItemStack stack);

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract float getMaxHealth();

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isClimbing() {
        if (this.isSpectator()) {
            return false;
        } else {
            BlockPos blockPos = this.getBlockPos();
            BlockState blockState = this.getBlockStateAtPos();
            if (blockState.isIn(BlockTags.CLIMBABLE)) {
                this.climbingPos = Optional.of(blockPos);
                return true;
            } else if (blockState.getBlock() instanceof TrapdoorBlock && this.canEnterTrapdoor(blockPos, blockState)) {
                this.climbingPos = Optional.of(blockPos);
                return true;
            } else {
                return InventoryPet.hasActivePet(this, SpiderPet.INSTANCE) && horizontalCollision;
            }
        }
    }

    private boolean collides(int x, int i, int z) {
        BlockPos pos = getBlockPos().add(x, i, z);
        return getBoundingBox().intersects(world.getBlockState(pos).getCollisionShape(world, pos).getBoundingBox());
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        int activePet = InventoryPet.getActivePet(this, CowPet.INSTANCE);
        if (!effect.getEffectType().isBeneficial() && activePet != -1) {
            PlayerEntity p = (PlayerEntity) ((Entity) this);
            if (!p.isCreative() && !p.isSpectator()) {
                ItemStack is = p.getInventory().getStack(activePet);
                is.setDamage(MathHelper.clamp(is.getDamage() + 2, 0, CowPet.INSTANCE.getMaxDamage()));
                p.getInventory().setStack(activePet, is);
            }
            return false;
        }
        activePet = InventoryPet.getActivePet(this, PigPet.INSTANCE);
        if ((effect.getEffectType() == StatusEffects.POISON ||
                    effect.getEffectType() == StatusEffects.HUNGER ||
                    effect.getEffectType() == StatusEffects.NAUSEA) && activePet != -1) {
            PlayerEntity p = (PlayerEntity) ((Entity) this);
            if (!p.isCreative() && !p.isSpectator()) {
                ItemStack is = p.getInventory().getStack(activePet);
                is.setDamage(MathHelper.clamp(is.getDamage() + 2, 0, PigPet.INSTANCE.getMaxDamage()));
                p.getInventory().setStack(activePet, is);
            }
            return false;
        }
        if (this.getGroup() == EntityGroup.UNDEAD) {
            StatusEffect statusEffect = effect.getEffectType();
            if (statusEffect == StatusEffects.REGENERATION || statusEffect == StatusEffects.POISON) {
                return false;
            }
        }
        return true;
    }

    @Inject(at = @At("RETURN"), method = "tryUseTotem", cancellable = true)
    public void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (checkSlimePetDeathProtection(source)) {
            cir.setReturnValue(true);
        }
    }

    private boolean checkSlimePetDeathProtection(DamageSource source) {
        if (source.bypassesProtection()) {
            return false;
        } else {
            int activePet = InventoryPet.getActivePet(this, SlimePet.INSTANCE);
            if (activePet != -1) {
                PlayerEntity p = (PlayerEntity) ((Object) this);
                if (!p.isCreative() && !isSpectator()) {
                    ItemStack is = p.getInventory().getStack(activePet);
                    p.increaseStat(Stats.USED.getOrCreateStat(SlimePet.INSTANCE), 1);
                    setHealth(getMaxHealth());
                    is.setDamage(Math.min(Math.max(is.getDamage() + 1, 0), is.getMaxDamage()));
                    p.getInventory().setStack(activePet, is);
                    world.sendEntityStatus(this, (byte) 99);
                    return true;
                } else return false;
            } else return false;
        }
    }
}