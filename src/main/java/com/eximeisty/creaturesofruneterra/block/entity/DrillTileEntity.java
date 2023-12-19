package com.eximeisty.creaturesofruneterra.block.entity;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.networking.ModMessages;
import com.eximeisty.creaturesofruneterra.networking.packet.S2CTremble;
import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class DrillTileEntity extends BlockEntity implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public int estado=0;
    public int ticks=0;
    public boolean inDesert=false;
    private static final AnimationBuilder TRANS_ANIM = new AnimationBuilder().addAnimation("animation.drill.trans", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.drill.drill", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder TRANS2_ANIM = new AnimationBuilder().addAnimation("animation.drill.trans2", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    public DrillTileEntity(BlockPos pos, BlockState state) {
        super(ModTiles.DRILL.get(), pos, state);
    }

	private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(event.getAnimatable().getTileData().getInt("state")==1){
            event.getController().setAnimation(TRANS_ANIM);
        }else if(event.getAnimatable().getTileData().getInt("state")==2){
            event.getController().setAnimation(TRANS2_ANIM);
        }
        return PlayState.CONTINUE;
	}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<DrillTileEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
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
        if(this.getTileData().getBoolean("shake")) {
            ModMessages.sendToClients(new S2CTremble(this.worldPosition));
        }
        if(this.getTileData().getInt("state")==1 && !this.level.isClientSide){
            ticks++;
            if(ticks==550) setEstado(false);
            if(inDesert){
                if(ticks==180) this.level.playSound(null, worldPosition, ModSoundEvents.REKSAI_AWAKEN.get(), SoundSource.AMBIENT, 3, 1);
                if(ticks==250) {
                    this.getTileData().putBoolean("shake", true);
                    this.level.players().forEach(pl ->{
                        if(pl.distanceToSqr(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ())<1200){
                            pl.sendMessage(new TextComponent("The floor trembles"), Util.NIL_UUID);
                        }
                    });
                }
                if(ticks==500){
                    ModEntityTypes.REKSAI.get().spawn(level.getServer().getLevel(level.dimension()), null, null, this.worldPosition, MobSpawnType.EVENT, false, false);
                    this.level.destroyBlock(worldPosition, false);
                }
            }else{
                if(ticks==250) {
                    this.level.players().forEach(pl ->{
                        if(pl.distanceToSqr(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ())<1200){
                            pl.sendMessage(new TextComponent("Nothing happens"), Util.NIL_UUID);
                        }
                    });
                }
            }
            if(ticks==1) if(this.level.getBiome(this.worldPosition).is(Biomes.DESERT)) inDesert=true;
        }else if(this.getTileData().getInt("state")==2){
            ticks++;
            if(ticks==30) setEstado(true);
        }
    }

    public void setEstado(boolean setZero) {
        ticks=0; inDesert=false;
        if(this.getTileData().getInt("state")==0 || this.getTileData().getInt("state")==2){ 
            this.estado=1; 
        }else{ 
            this.estado=2; 
        }
        if(setZero) this.estado=0;
        this.getTileData().putInt("state", estado);
        this.getTileData().putBoolean("shake", false);
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