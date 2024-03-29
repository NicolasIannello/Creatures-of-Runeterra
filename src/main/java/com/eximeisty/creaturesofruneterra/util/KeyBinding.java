package com.eximeisty.creaturesofruneterra.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_COR = "key.category.creaturesofruneterra.cor";
    public static final String KEY_FLY_UP = "key.creaturesofruneterra.fly_up";
    public static final String KEY_FLY_DOWN = "key.creaturesofruneterra.fly_down";
    public static final String KEY_ITEM_HABILITY = "key.creaturesofruneterra.item_hability";
    public static final String KEY_ITEM_HABILITY2 = "key.creaturesofruneterra.item_hability2";
    public static final String KEY_ARMOR_HABILITY = "key.creaturesofruneterra.armor_hability";

    public static final KeyMapping FLY_UP = new KeyMapping(KEY_FLY_UP, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KEY_CATEGORY_COR);
    public static final KeyMapping FLY_DOWN = new KeyMapping(KEY_FLY_DOWN, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_COR);
    public static final KeyMapping ITEM_HABILITY = new KeyMapping(KEY_ITEM_HABILITY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_COR);
    public static final KeyMapping ITEM_HABILITY2 = new KeyMapping(KEY_ITEM_HABILITY2, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, KEY_CATEGORY_COR);
    public static final KeyMapping ARMOR_HABILITY = new KeyMapping(KEY_ARMOR_HABILITY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_COR);
}
