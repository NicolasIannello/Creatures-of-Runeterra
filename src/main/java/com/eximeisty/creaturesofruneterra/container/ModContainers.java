package com.eximeisty.creaturesofruneterra.container;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CreaturesofRuneterra.MOD_ID);
    
    public static final RegistryObject<ContainerType<PorobotContainer>> POROBOT_CONTAINER = 
    CONTAINERS.register( "porobot_container", ()->IForgeContainerType.create(((windowId, inv, data)->{
        int id = data.readInt();
        //BlockPos id=data.readBlockPos();
        World world= inv.player.getEntityWorld();
        return new PorobotContainer(windowId, world, id, inv, inv.player);
    })));

    public static void register(IEventBus eventBus){
        CONTAINERS.register(eventBus);
    }
}