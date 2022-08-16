package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.Explosion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class HexcoreEntity extends ProjectileItemEntity{
    public HexcoreEntity(EntityType<? extends HexcoreEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public HexcoreEntity(World worldIn, LivingEntity throwerIn) {
        super(ModEntityTypes.HEXCORE.get(), throwerIn, worldIn);
    }
 
    public HexcoreEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.HEXCORE.get(), x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return ModItems.HEXCORE.get().asItem();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.func_213882_k();
        return (IParticleData)(itemstack.isEmpty() ? ParticleTypes.DRIPPING_WATER : new ItemParticleData(ParticleTypes.ITEM, itemstack));
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();
            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), (float)4);
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.getShooter());
            this.world.createExplosion((Entity)null, this.getPosX(), this.getPosY(), this.getPosZ(), (float)1, false, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }
 }