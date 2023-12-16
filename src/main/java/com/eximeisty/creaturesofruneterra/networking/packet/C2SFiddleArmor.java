package com.eximeisty.creaturesofruneterra.networking.packet;

import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;
import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class C2SFiddleArmor {

    public C2SFiddleArmor() { }

    public C2SFiddleArmor(FriendlyByteBuf buf) { }

    public void toBytes(FriendlyByteBuf buf) { }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer entityIn = context.getSender();
            Level worldIn = entityIn.level;
            FiddleArmorItem armor = (FiddleArmorItem)entityIn.getItemBySlot(EquipmentSlot.CHEST).getItem();
            List<Entity> list = Lists.newArrayList();
            worldIn.getEntities(null, entityIn.getBoundingBox().inflate(5)).stream().forEach(entity ->{
                if(list.size()<6 && entity!=entityIn)list.add(entity);
            });
            armor.targets=list;
        });
        return true;
    }
}

