package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.ModItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MisilEntity extends AbstractArrow implements IAnimatable{
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final AnimationBuilder DEFAULT_ANIM = new AnimationBuilder().addAnimation("animation.misil.default", ILoopType.EDefaultLoopTypes.LOOP);
    private int ticks=0;

    public MisilEntity(EntityType<? extends MisilEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public MisilEntity(Level worldIn, double x, double y, double z) {
        super(ModEntityTypes.MISIL.get(), x, y, z, worldIn);
    }

    public MisilEntity(Level worldIn, LivingEntity shooter) {
        super(ModEntityTypes.MISIL.get(), shooter, worldIn);
    }

    public void tick() {
        ticks++;
        if(ticks==100 && !level.isClientSide){
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            this.level.explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)2, flag, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
            this.discard();
        }
        Vec3 vec31 = this.getDeltaMovement();
        if(level.isClientSide) this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() - vec31.x, this.getY() - vec31.y + 0.15D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
        super.tick();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            this.attackBB(this.getBoundingBox().expandTowards(3, 3, 3).expandTowards(-3, -3, -3));
            this.level.explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)2, flag, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent(){
        return null;
    }

    protected void attackBB(AABB bb){
        this.level.getEntities(null, bb).stream().forEach(livingEntity -> {
            livingEntity.hurt(DamageSource.arrow(this, livingEntity), 4);
        });
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(DamageSource.arrow(this, entity1), 6.0F);
            if (entity1 instanceof LivingEntity) {
                EnchantmentHelper.doPostHurtEffects((LivingEntity) entity1, entity);
                EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, entity);
                //this.applyEnchantments((LivingEntity)entity1, entity);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.MISIL.get());
    }

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(DEFAULT_ANIM);
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
