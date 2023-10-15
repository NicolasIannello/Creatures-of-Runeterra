package com.eximeisty.creaturesofruneterra.entity.custom;


import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BulletEntity extends AbstractArrow implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public BulletEntity(EntityType<? extends BulletEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public BulletEntity(Level worldIn, double x, double y, double z) {
        super(ModEntityTypes.BULLET.get(), x, y, z, worldIn);
    }

    public BulletEntity(Level worldIn, LivingEntity shooter) {
        super(ModEntityTypes.BULLET.get(), shooter, worldIn);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if (!this.level.isClientSide) {
            Entity entity = p_37259_.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt( DamageSource.indirectMobAttack(this, (LivingEntity) entity1).setProjectile(), 4.0F);
            if (entity1 instanceof LivingEntity) {
                EnchantmentHelper.doPostHurtEffects((LivingEntity) entity, entity1);
                EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, entity);
            }
            this.doPostHurtEffects((LivingEntity) entity);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}