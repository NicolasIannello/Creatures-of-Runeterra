package com.eximeisty.creaturesofruneterra.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CTremble {
    private final BlockPos pos;

    public S2CTremble(BlockPos pos) {
        this.pos = pos;
    }

    public S2CTremble(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPlayerEntity pl = Minecraft.getInstance().player;
            if(pl.getDistanceSq(pos.getX(), pos.getY(), pos.getZ())<1200){
                pl.rotationYaw+=Math.random()*(3+3)-3;
                pl.rotationPitch+=Math.random()*(3+3)-3;
            }
        });
        return true;
    }
}