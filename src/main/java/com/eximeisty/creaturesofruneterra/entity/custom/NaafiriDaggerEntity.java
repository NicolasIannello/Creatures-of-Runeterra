package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class NaafiriDaggerEntity extends AbstractArrow implements IAnimatable {
    private final AnimationFactory cache = GeckoLibUtil.createFactory(this);
    private static final AnimationBuilder DEFAULT_ANIM = new AnimationBuilder().addAnimation("animation.naafiri_dagger.idle", ILoopType.EDefaultLoopTypes.LOOP);
    private int ticks=0;

    public NaafiriDaggerEntity(EntityType<? extends NaafiriDaggerEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public NaafiriDaggerEntity(Level worldIn, double x, double y, double z) {
        super(ModEntityTypes.NAAFIRI_DAGGER.get(), x, y, z, worldIn);
    }

    public NaafiriDaggerEntity(Level worldIn, LivingEntity shooter) {
        super(ModEntityTypes.NAAFIRI_DAGGER.get(), shooter, worldIn);
    }

    public void tick() {
        super.tick();
        ticks++;
        if(ticks>50) this.discard();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = getOwner();
            entity.hurt(DamageSource.mobAttack((LivingEntity) entity1), 6.0F);
            NaafiriHoundEntity hound= (NaafiriHoundEntity) ModEntityTypes.NAAFIRI_HOUND.get().spawn(this.level.getServer().getLevel(this.level.dimension()), (ItemStack) null, null, this.getOnPos().west(2).above(2), MobSpawnType.NATURAL, false, false);
            if(this.getOwner()!=null){
                hound.setTarget(((NaafiriEntity) this.getOwner()).getTarget());
                hound.tame((Player) ((NaafiriEntity) this.getOwner()).getOwner());
            }
            this.discard();
        }
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(DEFAULT_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    protected ItemStack getPickupItem() { return ItemStack.EMPTY; }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.cache;
    }
}
