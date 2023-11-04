package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DBShieldEntity extends AbstractArrowEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);
    private int ticks=0;
    private static final AnimationBuilder ANIM = new AnimationBuilder().addAnimation("animation.dbshield.new", true);

    public DBShieldEntity(EntityType<? extends DBShieldEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DBShieldEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.DBSHIELD.get(), x, y, z, worldIn);
    }

    public DBShieldEntity(World worldIn, LivingEntity shooter) {
        super(ModEntityTypes.DBSHIELD.get(), shooter, worldIn);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void tick() {
        ticks++;
        if(ticks==7){
            this.remove();
        }
        super.tick();
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
    }

    @Override
    protected SoundEvent getHitEntitySound(){
        return SoundEvents.BLOCK_CHORUS_FLOWER_DEATH;
    }

    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if (!this.world.isRemote) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getShooter();
            entity.attackEntityFrom(DamageSource.causeArrowDamage(this, entity1), 2.0F);
            if (entity1 instanceof LivingEntity) {
                this.applyEnchantments((LivingEntity)entity1, entity);
                //((LivingEntity)entity).setMotion(entity1.getLookVec().x*2, 0.2, entity1.getLookVec().z*2);
            }
        }
    }

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(ANIM);
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

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.DUNEBREAKER_SHIELD.get());
    }
}
