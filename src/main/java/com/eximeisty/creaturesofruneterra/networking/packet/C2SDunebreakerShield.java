package com.eximeisty.creaturesofruneterra.networking.packet;

import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDunebreakerShield {

    public C2SDunebreakerShield() { }

    public C2SDunebreakerShield(PacketBuffer buf) { }

    public void toBytes(PacketBuffer buf) { }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //DunebreakerShield clase = (DunebreakerShield)((Object)this);
            ServerPlayerEntity entityIn = context.getSender();
            World worldIn = entityIn.world;

            DBShieldEntity shieldEntity = new DBShieldEntity(worldIn, entityIn);
            shieldEntity.setDirectionAndMovement(entityIn, entityIn.rotationPitch, entityIn.rotationYaw, 0.0F,1.0F * 3.0F, 1.0F);
            shieldEntity.setDamage(2);
            shieldEntity.ticksExisted = 35;
            shieldEntity.setKnockbackStrength(2);
            shieldEntity.setNoGravity(true);
            shieldEntity.setPierceLevel((byte) 4);

            worldIn.addEntity(shieldEntity);

//            if (entityIn.getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString().contains("Dunebreaker")) {
//                clase.triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "dbs_controller", "attackd");
//            } else {
//                clase.triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "dbs_controller", "attacki");
//            }
        });
        return true;
    }
}

