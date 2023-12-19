package com.eximeisty.creaturesofruneterra.block.entity;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.networking.ModMessages;
import com.eximeisty.creaturesofruneterra.networking.packet.S2CTremble;
import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponent;
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
    private static final AnimationBuilder TRANS_ANIM = new AnimationBuilder().addAnimation("animation.drill.trans", false).addAnimation("animation.drill.drill", true);
    private static final AnimationBuilder TRANS2_ANIM = new AnimationBuilder().addAnimation("animation.drill.trans2", false);

    public DrillTileEntity() {
        super(ModTiles.DRILL.get());
    }

	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.getTileData().getInt("state")==1){
            event.getController().setAnimation(TRANS_ANIM);
        }else if(this.getTileData().getInt("state")==2){
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(this.world.getBlockState(this.getPos()), pkt.getNbtCompound());
    }

    @Override @SuppressWarnings("resource")
    public void tick() {
        if(this.getTileData().getBoolean("shake")) {
            ModMessages.sendToClients(new S2CTremble(this.getPos()));
        }
        if(this.getTileData().getInt("state")==1 && !this.world.isRemote){
            ticks++;
            if(ticks==550) setEstado(false);
            if(inDesert){
                if(ticks==180) this.world.playSound(null, pos, ModSoundEvents.REKSAI_AWAKEN.get(), SoundCategory.AMBIENT, 3, 1);
                if(ticks==250) {
                    this.getTileData().putBoolean("shake", true);
                    this.getTileData().putBoolean("shake", true);
                    this.world.getPlayers().forEach(pl ->{
                        if(pl.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ())<1200){
                            pl.sendMessage(new TranslationTextComponent("The floor trembles"), Util.DUMMY_UUID);
                        }
                    });
                }
                if(ticks==500){
                    ModEntityTypes.REKSAI.get().spawn(world.getServer().getWorld(world.getDimensionKey()), null, null, this.getPos(), SpawnReason.TRIGGERED, false, false);
                    this.world.destroyBlock(pos, false);
                }
            }else{
                if(ticks==250) {
                    this.world.getPlayers().forEach(pl ->{
                        if(pl.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ())<1200){
                            pl.sendMessage(new TranslationTextComponent("Nothing happens"), Util.DUMMY_UUID);
                        }
                    });
                }
            }
            if(ticks==1) if(this.world.getBiome(this.pos).getRegistryName().toString().contains("desert")) inDesert=true;
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
        //this.markDirty();
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
	}
}