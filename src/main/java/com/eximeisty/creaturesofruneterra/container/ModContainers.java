package com.eximeisty.creaturesofruneterra.container;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CreaturesofRuneterra.MOD_ID);
    
    public static final RegistryObject<MenuType<PorobotContainer>> POROBOT_CONTAINER =
    CONTAINERS.register( "porobot_container", ()->IForgeMenuType.create(((windowId, inv, data)->{
        int id = data.readInt();
        //BlockPos id=data.readBlockPos();
        Level world= inv.player.getLevel();
        return new PorobotContainer(windowId, world, id, inv, inv.player);
    })));

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        CONTAINERS.register(eventBus);
    }
}