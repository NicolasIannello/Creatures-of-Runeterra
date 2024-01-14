package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NaafiriDaggerEntity extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation DEFAULT_ANIM = RawAnimation.begin().then("animation.naafiri_dagger.idle", Animation.LoopType.LOOP);
    private int ticks=0;

    public NaafiriDaggerEntity(EntityType<? extends NaafiriDaggerEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public NaafiriDaggerEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.NAAFIRI_DAGGER.get(), x, y, z, worldIn);
    }

    public NaafiriDaggerEntity(Level worldIn, LivingEntity shooter) {
        super(ModEntities.NAAFIRI_DAGGER.get(), shooter, worldIn);
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
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = getOwner();
            entity.hurt( damageSources().mobProjectile(this, (LivingEntity) entity1), 6.0F);
            NaafiriHoundEntity hound= ModEntities.NAAFIRI_HOUND.get().spawn(this.level().getServer().getLevel(this.level().dimension()), (ItemStack) null, null, this.getOnPos().west(2).above(2), MobSpawnType.NATURAL, false, false);
            if(this.getOwner()!=null){
                hound.setTarget(((NaafiriEntity) this.getOwner()).getTarget());
                hound.tame((Player) ((NaafiriEntity) this.getOwner()).getOwner());
            }
            this.discard();
        }
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        event.getController().setAnimation(DEFAULT_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    protected ItemStack getPickupItem() { return ItemStack.EMPTY; }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
