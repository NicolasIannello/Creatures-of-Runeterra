package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
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

public class NaafiriDaggerEntity extends AbstractArrowEntity implements IAnimatable {
    private AnimationFactory cache = new AnimationFactory(this);
    private static final AnimationBuilder DEFAULT_ANIM = new AnimationBuilder().addAnimation("animation.naafiri_dagger.idle", true);
    private int ticks=0;

    public NaafiriDaggerEntity(EntityType<? extends NaafiriDaggerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public NaafiriDaggerEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.NAAFIRI_DAGGER.get(), x, y, z, worldIn);
    }

    public NaafiriDaggerEntity(World worldIn, LivingEntity shooter) {
        super(ModEntityTypes.NAAFIRI_DAGGER.get(), shooter, worldIn);
    }

    public void tick() {
        super.tick();
        ticks++;
        if(ticks>50) this.remove();
    }

//    @Override
//    public Packet<ClientGamePacketListener> getAddEntityPacket() {
//        Entity entity = this.getOwner();
//        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
//    }
    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.remove();
        }
    }

    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if (!this.world.isRemote) {
            Entity entity = result.getEntity();
            Entity entity1 = getShooter();
            entity.attackEntityFrom(DamageSource.causeArrowDamage(this, entity1), 6.0F);
            NaafiriHoundEntity hound= (NaafiriHoundEntity) ModEntityTypes.NAAFIRI_HOUND.get().spawn(world.getServer().getWorld(world.getDimensionKey()), (ItemStack) null, null, new BlockPos(this.getPosX()+2, this.getPosY()+2, this.getPosZ()), SpawnReason.NATURAL, false, false);
            if(this.getShooter()!=null){
                hound.setAttackTarget(((NaafiriEntity) this.getShooter()).getAttackTarget());
                hound.setTamedBy((PlayerEntity) ((NaafiriEntity) this.getShooter()).getOwner());
            }
            this.remove();
        }
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(DEFAULT_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    protected ItemStack getArrowStack() { return ItemStack.EMPTY; }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.cache;
    }
}
