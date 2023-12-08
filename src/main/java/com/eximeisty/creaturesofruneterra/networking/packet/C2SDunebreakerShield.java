package com.eximeisty.creaturesofruneterra.networking.packet;

import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDunebreakerShield {

    public C2SDunebreakerShield() { }

    public C2SDunebreakerShield(FriendlyByteBuf buf) { }

    public void toBytes(FriendlyByteBuf buf) { }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //DunebreakerShield clase = (DunebreakerShield)((Object)this);
            ServerPlayer entityIn = context.getSender();
            Level worldIn = entityIn.level();

            DBShieldEntity shieldEntity = new DBShieldEntity(worldIn, entityIn);
            shieldEntity.shootFromRotation(entityIn, entityIn.getRotationVector().x, entityIn.getRotationVector().y, 0.0F, 3.0F, 1.0F);
            shieldEntity.setBaseDamage(2);
            shieldEntity.tickCount = 35;
            shieldEntity.setKnockback(2);
            shieldEntity.setNoGravity(true);
            shieldEntity.setPierceLevel((byte) 4);

            worldIn.addFreshEntity(shieldEntity);

//            if (entityIn.getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString().contains("Dunebreaker")) {
//                clase.triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "dbs_controller", "attackd");
//            } else {
//                clase.triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "dbs_controller", "attacki");
//            }
        });
        return true;
    }
}

