package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class HexcoreEntity extends ThrowableItemProjectile {
    public HexcoreEntity(EntityType<? extends HexcoreEntity> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public HexcoreEntity(Level p_37399_, LivingEntity p_37400_) {
        super(ModEntities.HEXCORE.get(), p_37400_, p_37399_);
    }

    public HexcoreEntity(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(ModEntities.HEXCORE.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HEXCORE.get().asItem();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        //Entity entity = this.getOwner();
        //return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (itemstack.isEmpty() ? ParticleTypes.DRIPPING_WATER : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();
            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getRandomX(0.2), this.getY(), this.getRandomZ(0.2), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onHitEntity(EntityHitResult p_37404_) {
        super.onHitEntity(p_37404_);
        Entity entity = p_37404_.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)4);
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        if (!this.level.isClientSide) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            this.level.explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)1, flag, flag ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
            this.level.broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

}
