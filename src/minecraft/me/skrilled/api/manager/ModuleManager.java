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
import me.skrilled.api.modules.module.combat.AutoClicker;
import me.skrilled.api.modules.module.misc.*;
import me.skrilled.api.modules.module.move.AutoJump;
import me.skrilled.api.modules.module.move.AutoSprint;
import me.skrilled.api.modules.module.render.ESP;
import me.skrilled.api.modules.module.render.HUD;
import me.skrilled.api.modules.module.render.Nametag;
import me.skrilled.api.modules.module.render.SettingMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    public ArrayList<ModuleHeader> mList = new ArrayList<>();


    public void load() {
        EventManager.register(this);
        //Render
        mList.add(new HUD());
        mList.add(new ESP());
        mList.add(new SettingMenu());
        mList.add(new Nametag());
        //Move
        mList.add(new AutoSprint());
        mList.add(new AutoJump());
        //Combat
        mList.add(new AutoClicker());
        //Test
        Collections.addAll(mList, new TestModule001(), new TestModule002(), new TestModule003(), new TestModule004(), new TestModule005(), new TestModule006(), new TestModule007(), new TestModule008());


    }

    public ModuleHeader getModuleByName(String mName) {
        for (ModuleHeader moduleHeader : this.mList) {
            if (moduleHeader.getModuleName().equalsIgnoreCase(mName)) {
                System.out.println("成功-返还->" + moduleHeader.getModuleName());
                return moduleHeader;
            }
        }
        System.out.println("失败-返还->" + mName);
        return null;

    }

    public ModuleHeader getModuleByClass(Class<? extends ModuleHeader> mClass) {
        for (ModuleHeader moduleHeader : this.mList) {
            if (moduleHeader.getClass() == mClass) return moduleHeader;
        }
        return null;
    }

    public ModuleHeader getModuleByIndex(int index) {
        for (ModuleHeader moduleHeader : this.mList) {
            if (mList.get(index) == moduleHeader) return moduleHeader;
        }
        return null;
    }

    public List<ModuleHeader> getModuleListByModuleType(ModuleHeader.ModuleType moduleType) {
        return mList.stream().filter(moduleHeader -> moduleHeader.getModuleType() == moduleType).collect(Collectors.toList());
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
