package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MisilEntity extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation DEFAULT_ANIM = RawAnimation.begin().then("animation.misil.default", Animation.LoopType.LOOP);
    private int ticks=0;

    public MisilEntity(EntityType<? extends MisilEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public MisilEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.MISIL.get(), x, y, z, worldIn);
    }

    public MisilEntity(Level worldIn, LivingEntity shooter) {
        super(ModEntities.MISIL.get(), shooter, worldIn);
    }

    public void tick() {
        ticks++;
        if(ticks==100 && !level.isClientSide){
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            this.level.explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)2, flag, flag ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
            this.discard();
        }
        Vec3 vec31 = this.getDeltaMovement();
        if(level.isClientSide) this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() - vec31.x, this.getY() - vec31.y + 0.15D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
        super.tick();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            this.attackBB(this.getBoundingBox().expandTowards(3, 3, 3).expandTowards(-3, -3, -3));
            this.level.explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)2, flag, flag ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    protected void attackBB(AABB bb){
        this.level.getEntities(null, bb).stream().forEach(livingEntity -> {
            livingEntity.hurt(damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 4);
        });
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt( damageSources().mobProjectile(this, (LivingEntity) entity1), 6.0F);
            if (entity1 instanceof LivingEntity) {
                EnchantmentHelper.doPostHurtEffects((LivingEntity) entity, entity1);
                EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, entity);
                //this.applyEnchantments((LivingEntity)entity1, entity);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() { return new ItemStack(ModItems.MISIL.get()); }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        event.getController().setAnimation(DEFAULT_ANIM);
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
}
