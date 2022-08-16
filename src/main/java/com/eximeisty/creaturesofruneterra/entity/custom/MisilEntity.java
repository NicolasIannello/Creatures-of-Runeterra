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
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MisilEntity extends AbstractArrowEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);

    public MisilEntity(EntityType<? extends MisilEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public MisilEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.MISIL.get(), x, y, z, worldIn);
    }

    public MisilEntity(World worldIn, LivingEntity shooter) {
        super(ModEntityTypes.MISIL.get(), shooter, worldIn);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.getShooter());
            this.world.createExplosion((Entity)null, this.getPosX(), this.getPosY(), this.getPosZ(), (float)2, flag, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            this.remove();
        }
    }

    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if (!this.world.isRemote) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getShooter();
            entity.attackEntityFrom(DamageSource.causeArrowDamage(this, entity1), 6.0F);
            if (entity1 instanceof LivingEntity) {
                this.applyEnchantments((LivingEntity)entity1, entity);
            }
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.MISIL.get());
    }

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.misil.default", true));
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