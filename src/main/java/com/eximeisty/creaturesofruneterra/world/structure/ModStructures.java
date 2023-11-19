package com.eximeisty.creaturesofruneterra.world.structure;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.world.structure.structures.DarkinPedestal;
import com.eximeisty.creaturesofruneterra.world.structure.structures.VoidSandCave;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<StructureFeature<?>> VOIDSANDCAVE = DEFERRED_REGISTRY_STRUCTURE.register("sand_entrance", VoidSandCave::new);

    public static final RegistryObject<StructureFeature<?>> DARKINPEDESTAL = DEFERRED_REGISTRY_STRUCTURE.register("darkin_pedestal", DarkinPedestal::new);

    public static void register(IEventBus eventBus) {
        DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
    }
}
