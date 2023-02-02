/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.manager;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.skrilled.api.event.EventKey;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.combat.Aura;
import me.skrilled.api.modules.module.combat.AutoClicker;
import me.skrilled.api.modules.module.combat.HitBox;
import me.skrilled.api.modules.module.combat.Reach;
import me.skrilled.api.modules.module.misc.TestModule001;
import me.skrilled.api.modules.module.misc.TestModule002;
import me.skrilled.api.modules.module.move.AutoJump;
import me.skrilled.api.modules.module.move.AutoSprint;
import me.skrilled.api.modules.module.player.TestModule003;
import me.skrilled.api.modules.module.render.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    public static ArrayList<ModuleHeader> mList = new ArrayList<>();

    public static ModuleHeader getModuleByName(String mName) {
        for (ModuleHeader moduleHeader : mList) {
            if (moduleHeader.getModuleName().equalsIgnoreCase(mName)) {
//                System.out.println("成功-返还->" + moduleHeader.getModuleName());
                return moduleHeader;
            }
        }
//        System.out.println("失败-返还->" + mName);
        return null;

    }

    public void load() {
        EventManager.register(this);
        //Render
        mList.add(new HUD());
        mList.add(new ESP());
        mList.add(new Nametags());
        mList.add(new Chams());
        mList.add(new RenderModifier());
        mList.add(new WorldRenderEditor());
        mList.add(new MouseOverlyRender());
        //Move
        mList.add(new AutoSprint());
        mList.add(new AutoJump());
        //Combat
        mList.add(new AutoClicker());
        mList.add(new Aura());
        mList.add(new Reach());
        mList.add(new HitBox());


        mList.add(new SettingMenu());
        //Test
        Collections.addAll(mList, new TestModule001(), new TestModule002(),new TestModule003());
        for (ModuleHeader moduleHeader : mList) {
            moduleHeader.loadValueLists();
        }
    }

    public ModuleHeader getModuleByClass(Class<? extends ModuleHeader> mClass) {
        for (ModuleHeader moduleHeader : mList) {
            if (moduleHeader.getClass() == mClass) return moduleHeader;
        }
        return null;
    }

    public ModuleHeader getModuleByIndex(int index) {
        for (ModuleHeader moduleHeader : mList) {
            if (mList.get(index) == moduleHeader) return moduleHeader;
        }
        return null;
    }

    public ArrayList<ModuleHeader> getModuleListByModuleType(ModuleType moduleType) {
        List<ModuleHeader> list = mList.stream().filter(moduleHeader -> moduleHeader.getModuleType() == moduleType).collect(Collectors.toList());
        return new ArrayList<>(list);
    }

    @EventTarget
    public void onKey(EventKey eventKey) {
        for (ModuleHeader moduleHeader : mList) {
            if (eventKey.getKey() == moduleHeader.getKey()) {
                moduleHeader.toggle();
            }
        }
    }
}
