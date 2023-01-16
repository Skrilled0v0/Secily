package me.skrilled.api.modules.module.render;

import com.darkmagician6.eventapi.EventTarget;
import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.api.event.EventRender2D;
import me.skrilled.api.event.EventUpdate;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.Colors;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nametags extends ModuleHeader {
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<>();

    public ValueHeader invis = new ValueHeader("Invisible", false);
    public ValueHeader armor = new ValueHeader("Armor", false);

    public Nametags() {
        super("Nametags", false, ModuleType.RENDER);
        this.addValueList(invis, armor);
        setKey(Keyboard.KEY_N);
    }

    @EventTarget
    public void update(EventUpdate event) {
        updatePositions();

    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        GlStateManager.pushMatrix();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        for (Entity ent : entityPositions.keySet()) {
            if (ent != mc.thePlayer && ((boolean) this.getValue(invis) || !ent.isInvisible())) {

                GlStateManager.pushMatrix();
                if ((ent instanceof EntityPlayer)) {

                    double[] renderPositions = entityPositions.get(ent);
                    if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
                        GlStateManager.popMatrix();
                        continue;
                    }

                    CFontRenderer font = sense.getFontBuffer().font18;

                    GlStateManager.translate(renderPositions[0] / scaledRes.getScaleFactor(), renderPositions[1] / scaledRes.getScaleFactor(), 0.0D);
                    scale();
                    GlStateManager.translate(0.0D, -2.5D, 0.0D);

                    String health = "Health: " + Math.round(((EntityLivingBase) ent).getHealth() * 10) / 10;
//                    (AntiBot.isBot(ent) ? "\2479[BOT]" : "") + (Teams.isOnSameTeam(ent) ? "\247b[TEAM]" : "") + "\247r" +
                    String str = ent.getDisplayName().getUnformattedText();
                    String suff = "";


                    str = str + suff;

                    float strWidth = font.getStringWidth(str.replaceAll("\247.", ""));
                    float strWidth2 = sense.getFontBuffer().font10.getStringWidth(health);
                    float allWidth = (Math.max(strWidth, strWidth2)) + 8;
                    RenderUtil.drawRect(-allWidth / 2, -25.0f, allWidth / 2, 0, Colors.getColor(0, 130));
                    font.drawString(str, -allWidth / 2 + 4, -22.0F, Colors.WHITE.c);

                    sense.getFontBuffer().font10.drawString(health, -allWidth / 2 + 4, -10.0F, Colors.WHITE.c);

                    EntityLivingBase entity = (EntityLivingBase) ent;
                    float nowhealth = (float) Math.ceil(entity.getHealth() + entity.getAbsorptionAmount());
                    float maxHealth = entity.getMaxHealth() + entity.getAbsorptionAmount();
                    float healthP = nowhealth / maxHealth;
                    RenderUtil.drawRect(-allWidth / 2, -1, allWidth / 2, 0, Colors.getColor(100, 0, 0, 255));
                    RenderUtil.drawRect(-allWidth / 2, -1, allWidth / 2 - ((allWidth / 2) * (1 - healthP)) * 2, 0, Colors.RED.c);

                    if ((boolean) this.getValue(armor)) {
                        List<ItemStack> itemsToRender = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(i);
                            if (stack != null) {
                                itemsToRender.add(stack);
                            }
                        }
                        int x = -(itemsToRender.size() * 9);
                        for (ItemStack stack : itemsToRender) {
                            RenderHelper.enableGUIStandardItemLighting();
                            mc.getRenderItem().renderItemIntoGUI(stack, x + 6, -42);
                            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, -42);
                            x += 3;
                            RenderHelper.disableStandardItemLighting();
                            if (stack != null) {
                                int y = 21;
                                int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                                int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                                int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
                                if (sLevel > 0) {
                                    drawEnchantTag("Sh" + getColor(sLevel) + sLevel, x, y);
                                    y += 6;
                                }
                                if (fLevel > 0) {
                                    drawEnchantTag("Fir" + getColor(fLevel) + fLevel, x, y);
                                    y += 6;
                                }
                                if (kLevel > 0) {
                                    drawEnchantTag("Kb" + getColor(kLevel) + kLevel, x, y);
                                } else if ((stack.getItem() instanceof ItemArmor)) {
                                    int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
                                    int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                                    int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                                    if (pLevel > 0) {
                                        drawEnchantTag("P" + getColor(pLevel) + pLevel, x, y);
                                        y += 6;
                                    }
                                    if (tLevel > 0) {
                                        drawEnchantTag("Th" + getColor(tLevel) + tLevel, x, y);
                                        y += 6;
                                    }
                                    if (uLevel > 0) {
                                        drawEnchantTag("Unb" + getColor(uLevel) + uLevel, x, y);
                                    }
                                } else if ((stack.getItem() instanceof ItemBow)) {
                                    int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                                    int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                                    int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                                    if (powLevel > 0) {
                                        drawEnchantTag("Pow" + getColor(powLevel) + powLevel, x, y);
                                        y += 6;
                                    }
                                    if (punLevel > 0) {
                                        drawEnchantTag("Pun" + getColor(punLevel) + punLevel, x, y);
                                        y += 6;
                                    }
                                    if (fireLevel > 0) {
                                        drawEnchantTag("Fir" + getColor(fireLevel) + fireLevel, x, y);
                                    }
                                } else if (stack.getRarity() == EnumRarity.EPIC) {
                                    drawEnchantTag("\2476\247lGod", x - 2, y);
                                }
                                int var7 = (int) Math.round(255.0D - (double) stack.getItemDamage() * 255.0D / (double) stack.getMaxDamage());
                                int var10 = 255 - var7 << 16 | var7 << 8;

                                float x2 = (float) (x * 1.05D) - 2;
                                if ((stack.getMaxDamage() - stack.getItemDamage()) > 0) {
                                    GlStateManager.pushMatrix();
                                    GlStateManager.disableDepth();
                                    //Hanabi.INSTANCE.fontManager.comfortaa12.drawString("" + (stack.getMaxDamage() - stack.getItemDamage()), x2 + 6, -32, customColor.getRGB());
                                    GlStateManager.enableDepth();
                                    GlStateManager.popMatrix();
                                }

                                x += 12;
                            }
                        }
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }

    private void drawEnchantTag(String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x = (int) (x * 1.05D);
        y -= 6;
        sense.getFontBuffer().font10.drawString(text, x + 9, -30 - y, Colors.getColor(255));
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private String getColor(int level) {
        if (level == 1) {

        } else if (level == 2) {
            return "\247a";
        } else if (level == 3) {
            return "\2473";
        } else if (level == 4) {
            return "\2474";
        } else if (level >= 5) {
            return "\2476";
        }
        return "\247f";
    }

    private void scale() {
        float scale = 1;
        scale *= mc.gameSettings.smoothCamera ? 2 : 1;
        GlStateManager.scale(scale, scale, scale);
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer)) && ((!ent.isInvisible()) || (!(boolean) this.getValue(invis)))) {
//                System.out.println(ent);
                double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - mc.getRenderManager().viewerPosX;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
                double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - mc.getRenderManager().viewerPosZ;
                y += ent.height + 0.2D;
                System.out.println(convertTo2D(x, y, z)[0]);
//                if ((convertTo2D(x, y, z)[2] >= 0.0D) && (convertTo2D(x, y, z)[2] < 1.0D)) {
                double[] pos = {convertTo2D(x, y, z)[0],
                        convertTo2D(x, y, z)[1],
                        Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]),
                        convertTo2D(x, y, z)[2]};
                entityPositions.put((EntityPlayer) ent, pos);
//                }
            }
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = mc.timer.renderPartialTicks;
        float prevYaw = mc.thePlayer.rotationYaw;
        float prevPrevYaw = mc.thePlayer.prevRotationYaw;
        float[] rotations = getRotationFromPosition(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks, ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks, ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
        mc.getRenderViewEntity().rotationYaw = (mc.getRenderViewEntity().prevRotationYaw = rotations[0]);
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = convertTo2D(x, y, z);
        mc.getRenderViewEntity().rotationYaw = prevYaw;
        mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }
}
