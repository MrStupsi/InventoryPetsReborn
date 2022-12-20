package de.mrstupsi.inventorypetsreborn.mixin;

import de.mrstupsi.inventorypetsreborn.item.*;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract boolean trapdoorUsableAsLadder(BlockPos p_21177_, BlockState p_21178_);

    @Shadow public abstract MobType getMobType();

    @Shadow private Optional<BlockPos> lastClimbablePos;

    @Shadow public abstract ItemStack getItemInHand(InteractionHand p_21121_);

    @Shadow public abstract void setHealth(float p_21154_);

    @Shadow public abstract float getMaxHealth();

    @Shadow protected abstract boolean checkTotemDeathProtection(DamageSource p_21263_);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean onClimbable() {
        if (this.isSpectator()) {
            return false;
        } else {
            BlockPos blockpos = this.blockPosition();
            BlockState blockstate = this.getFeetBlockState();
            Optional<BlockPos> ladderPos = net.minecraftforge.common.ForgeHooks.isLivingOnLadder(blockstate, level, blockpos, (LivingEntity) ((Entity) this));
            if (ladderPos.isPresent()) {
                this.lastClimbablePos = ladderPos;
                return true;
            } else {
                return InventoryPet.hasActivePet(this, SpiderPet.INSTANCE) && horizontalCollision;
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canBeAffected(MobEffectInstance effect) {
        int activePet = InventoryPet.getActivePet(this, CowPet.INSTANCE);
        if (!effect.getEffect().isBeneficial() && activePet != -1) {
            Player p = (Player) ((Entity) this);
            if (!p.isCreative() && !p.isSpectator()) {
                ItemStack is = p.getInventory().getItem(activePet);
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 2, 0), is.getMaxDamage()));
                p.getInventory().setItem(activePet, is);
            }
            return false;
        }
        activePet = InventoryPet.getActivePet(this, PigPet.INSTANCE);
        if ((effect.getEffect() == MobEffects.POISON ||
             effect.getEffect() == MobEffects.HUNGER ||
             effect.getEffect() == MobEffects.CONFUSION) && activePet != -1) {
            Player p = (Player) ((Entity) this);
            if (!p.isCreative() && !p.isSpectator()) {
                ItemStack is = p.getInventory().getItem(activePet);
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 2, 0), is.getMaxDamage()));
                p.getInventory().setItem(activePet, is);
            }
            return false;
        }
        if (this.getMobType() == MobType.UNDEAD) {
            MobEffect statusEffect = effect.getEffect();
            if (statusEffect == MobEffects.REGENERATION || statusEffect == MobEffects.POISON) {
                return false;
            }
        }
        return true;
    }

    @Inject(at = @At("RETURN"), method = "checkTotemDeathProtection", cancellable = true)
    public void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (checkSlimePetDeathProtection(source)) {
            cir.setReturnValue(true);
        }
    }

    private boolean checkSlimePetDeathProtection(DamageSource source) {
        if (source.isBypassInvul()) {
            return false;
        } else {
            int activePet = InventoryPet.getActivePet(this, SlimePet.INSTANCE);
            if (activePet != -1) {
                Player p = (Player) ((Object) this);
                if (!p.isCreative() && !isSpectator()) {
                    ItemStack is = p.getInventory().getItem(activePet);
                    p.awardStat(Stats.ITEM_USED.get(SlimePet.INSTANCE), 1);
                    setHealth(getMaxHealth());
                    is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                    p.getInventory().setItem(activePet, is);
                    level.broadcastEntityEvent(this, (byte) 99);
                    return true;
                } else return false;
            } else return false;
        }
    }
}