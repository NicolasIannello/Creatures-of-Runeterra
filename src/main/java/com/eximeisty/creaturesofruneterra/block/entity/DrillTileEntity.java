package com.eximeisty.creaturesofruneterra.block.entity;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DrillTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity{
    private final AnimationFactory factory = new AnimationFactory(this);
    public int estado=0;
    public int ticks=0;
    public boolean inDesert=false;

    public DrillTileEntity() {
        super(ModTiles.DRILL.get());
    }

	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.getTileData().getInt("state")==1){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.drill.trans", false).addAnimation("animation.drill.drill", true));
        }else if(this.getTileData().getInt("state")==2){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.drill.trans2", false));
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(this.world.getBlockState(this.getPos()), pkt.getNbtCompound());
    }
    
    @Override
    public void tick() {
        if(this.estado==1){
            ticks++;
            if(ticks==550) setEstado(false);
            if(inDesert){
                if(ticks==180) this.world.playSound(null, pos, ModSoundEvents.REKSAI_AWAKEN.get(), SoundCategory.AMBIENT, 3, 1);
                if(ticks==250) this.getWorld().getServer().getPlayerList().sendPacketToAllPlayers(new SChatPacket(new TranslationTextComponent("The floor trembles"), ChatType.CHAT, Util.DUMMY_UUID));
                if(ticks==500){
                    RekSaiEntity reksai=new RekSaiEntity(ModEntityTypes.REKSAI.get(), this.world);
                    reksai.setPosition(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                    this.world.addEntity(reksai);
                    this.world.destroyBlock(pos, false);
                }
            }else{
                if(ticks==250) this.getWorld().getServer().getPlayerList().sendPacketToAllPlayers(new SChatPacket(new TranslationTextComponent("Nothing happens"), ChatType.CHAT, Util.DUMMY_UUID));
            }
            if(ticks==1) if(this.world.getBiome(this.pos).getRegistryName().toString().contains("desert")) inDesert=true;
        }else if(this.estado==2){
            ticks++;
            if(ticks==30) setEstado(true);
        }
    }

    public void setEstado(boolean setZero) {
        ticks=0; inDesert=false;
        if(this.estado==0 || this.estado==2){ 
            this.estado=1; 
        }else{ 
            this.estado=2; 
        }
        if(setZero) this.estado=0;
        this.getTileData().putInt("state", estado);
        //this.markDirty();
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
	}
}