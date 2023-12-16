package com.eximeisty.creaturesofruneterra.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CTremble {
    private final BlockPos pos;

    public S2CTremble(BlockPos pos) {
        this.pos = pos;
    }

    public S2CTremble(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LocalPlayer pl = Minecraft.getInstance().player;
            if(pl.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())<1200){
                pl.setXRot(pl.getXRot() + (float) Math.random() * (3 + 3) - 3);
                pl.setYRot(pl.getYRot() + (float) Math.random() * (3 + 3) - 3);
            }
        });
        return true;
    }
}