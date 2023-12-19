package com.eximeisty.creaturesofruneterra.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY_COR = "key.category.creaturesofruneterra.cor";
    public static final String KEY_FLY_UP = "key.creaturesofruneterra.fly_up";
    public static final String KEY_FLY_DOWN = "key.creaturesofruneterra.fly_down";
    public static final String KEY_ITEM_HABILITY = "key.creaturesofruneterra.item_hability";
    public static final String KEY_ARMOR_HABILITY = "key.creaturesofruneterra.armor_hability";

    public static final KeyBinding FLY_UP = new KeyBinding(KEY_FLY_UP, KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KEY_CATEGORY_COR);
    public static final KeyBinding FLY_DOWN = new KeyBinding(KEY_FLY_DOWN, KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_COR);
    public static final KeyBinding ITEM_HABILITY = new KeyBinding(KEY_ITEM_HABILITY, KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_COR);
    public static final KeyBinding ARMOR_HABILITY = new KeyBinding(KEY_ARMOR_HABILITY, KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_COR);
}
