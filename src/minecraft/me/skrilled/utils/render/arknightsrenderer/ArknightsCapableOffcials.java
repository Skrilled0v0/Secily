/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230201
 */
package me.skrilled.utils.render.arknightsrenderer;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

// TODO: 2023/2/1 明日方舟干员模型构造器
//  动作有 Move（移动）Interact（点击）Relax（闲置）Special（特殊、随机触发）每种对应一套动作的 ResourceLocation List
public class ArknightsCapableOffcials {
    ArrayList<ResourceLocation> resourceLocations;
    int maxIndex;
    String name;

    public ArknightsCapableOffcials(String name, ArrayList<ResourceLocation> resourceLocations, int maxIndex) {
        this.resourceLocations = resourceLocations;
        this.maxIndex = maxIndex;
        this.name = name;
    }
}
