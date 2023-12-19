package com.eximeisty.creaturesofruneterra.networking.packet;

import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class C2SFiddleArmor {

    public C2SFiddleArmor() { }

    public C2SFiddleArmor(PacketBuffer buf) { }

    public void toBytes(PacketBuffer buf) { }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity entityIn = context.getSender();
            World worldIn = entityIn.world;
            FiddleArmorItem armor = (FiddleArmorItem)entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem();
            List<Entity> list = Lists.newArrayList();
            worldIn.getEntitiesWithinAABB(Entity.class, entityIn.getBoundingBox().grow(5)).stream().forEach(entity ->{
                if(list.size()<6 && entity!=entityIn)list.add(entity);
            });
            armor.targets=list;
        });
        return true;
    }
}

