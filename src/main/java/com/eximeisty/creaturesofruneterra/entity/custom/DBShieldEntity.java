package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DBShieldEntity extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int ticks=0;
    private static final RawAnimation ANIM = RawAnimation.begin().then("animation.dbshield.new", Animation.LoopType.LOOP);

    public DBShieldEntity(EntityType<? extends DBShieldEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public DBShieldEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.DBSHIELD.get(), x, y, z, worldIn);
    }

    public DBShieldEntity(Level worldIn, LivingEntity shooter) {
        super(ModEntities.DBSHIELD.get(), shooter, worldIn);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    public void tick() {
        ticks++;
        if(ticks==7){
            this.discard();
        }
        super.tick();
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.CHORUS_FLOWER_DEATH;
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt( damageSources().mobProjectile(this, (LivingEntity) entity1), 2.0F);
            entity.push(this.getLookAngle().x*2,0.2,this.getLookAngle().z*2);
            if (entity1 instanceof LivingEntity) {
                EnchantmentHelper.doPostHurtEffects((LivingEntity) entity, entity1);
                EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, entity);
            }
        }
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        event.getController().setAnimation(ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    @Override
    protected ItemStack getPickupItem() { return new ItemStack(ModItems.MISIL.get()); }

}
