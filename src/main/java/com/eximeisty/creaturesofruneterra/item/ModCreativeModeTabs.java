package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab COR_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        COR_TAB = event.registerCreativeModeTab(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "cor_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.GEMSTONE.get())).title(Component.translatable("creativemodetab.cor_tab")));
    }
}