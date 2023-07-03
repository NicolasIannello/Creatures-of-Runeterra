package com.eximeisty.creaturesofruneterra.util;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent>  SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<SoundEvent> REKSAI_APP = registerSoundEvent("reksai_app");
    public static final RegistryObject<SoundEvent> REKSAI_AWAKEN = registerSoundEvent("reksai_awaken");
    public static final RegistryObject<SoundEvent> REKSAI_ESCAPE = registerSoundEvent("reksai_escape");
    public static final RegistryObject<SoundEvent> REKSAI_HIT = registerSoundEvent("reksai_hit");
    public static final RegistryObject<SoundEvent> REKSAI_JUMP = registerSoundEvent("reksai_jump");
    public static final RegistryObject<SoundEvent> REKSAI_ANGRY = registerSoundEvent("reksai_angry");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, ()-> new SoundEvent(new ResourceLocation(CreaturesofRuneterra.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}