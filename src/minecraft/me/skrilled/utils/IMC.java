/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.utils;

import me.skrilled.SenseHeader;
import net.minecraft.client.Minecraft;

public interface IMC {
    Minecraft mc = Minecraft.getMinecraft();
    SenseHeader sense = SenseHeader.getSense;
}
