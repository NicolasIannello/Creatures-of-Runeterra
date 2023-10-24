package com.eximeisty.creaturesofruneterra.block.entity;

import com.eximeisty.creaturesofruneterra.block.ModTiles;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.util.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class DrillTileEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int estado=0;
    public int ticks=0;
    public boolean inDesert=false;
    private static final RawAnimation TRANS_ANIM = RawAnimation.begin().then("animation.drill.trans", Animation.LoopType.PLAY_ONCE).then("animation.drill.drill", Animation.LoopType.LOOP);
    private static final RawAnimation TRANS2_ANIM = RawAnimation.begin().then("animation.drill.trans2", Animation.LoopType.PLAY_ONCE);

    public DrillTileEntity(BlockPos pos, BlockState state) {
        super(ModTiles.DRILL.get(), pos, state);
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        if(((DrillTileEntity)event.getAnimatable()).getPersistentData().getInt("state")==1){
            event.getController().setAnimation(TRANS_ANIM);
        }else if(((DrillTileEntity)event.getAnimatable()).getPersistentData().getInt("state")==2){
            event.getController().setAnimation(TRANS2_ANIM);
        }
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

    @Override @Nullable
    public ClientboundBlockEntityDataPacket  getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag tag = pkt.getTag();
        if(tag != null){
            load(tag);
        }
    }

    public void tick() {
        if(this.getPersistentData().getBoolean("shake")) {
            LocalPlayer pl = Minecraft.getInstance().player;
//            this.level.players().forEach(player ->{
                if(pl.distanceToSqr(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ())<400){
                    pl.setXRot(pl.getXRot()+(float)Math.random()*(3+3)-3);
                    pl.setYRot(pl.getYRot()+(float)Math.random()*(3+3)-3);
                }
//            });
        }
        if(this.getPersistentData().getInt("state")==1 && !this.level.isClientSide){
            ticks++;
            if(ticks==550) setEstado(false);
            if(inDesert){
                if(ticks==180) this.level.playSound(null, worldPosition, ModSounds.REKSAI_AWAKEN.get(), SoundSource.AMBIENT, 3, 1);
                if(ticks==250) {
                    this.getPersistentData().putBoolean("shake", true);
                    this.getLevel().getServer().getPlayerList().broadcastSystemMessage(Component.translatable("The floor trembles"), true);
                }
                if(ticks==500){
                    ModEntities.REKSAI.get().spawn(level.getServer().overworld(), (ItemStack) null, null, this.worldPosition, MobSpawnType.EVENT, false, false);
                    this.level.destroyBlock(worldPosition, false);
                }
            }else{
                if(ticks==250) this.getLevel().getServer().getPlayerList().broadcastSystemMessage(Component.translatable("Nothing happens"), true);
            }
            if(ticks==1) if(this.level.getBiome(this.worldPosition).is(Biomes.DESERT)) inDesert=true;
        }else if(this.getPersistentData().getInt("state")==2){
            ticks++;
            if(ticks==30) setEstado(true);
        }
    }

    public void setEstado(boolean setZero) {
        ticks=0; inDesert=false;
        if(this.getPersistentData().getInt("state")==0 || this.getPersistentData().getInt("state")==2){
            this.estado=1; 
        }else{ 
            this.estado=2; 
        }
        if(setZero) this.estado=0;
        this.getPersistentData().putInt("state", estado);
        this.getPersistentData().putBoolean("shake", false);
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
    }
}