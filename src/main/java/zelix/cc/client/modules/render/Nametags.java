package zelix.cc.client.modules.render;

import org.spongepowered.asm.mixin.Overwrite;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.render.Event3D;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.combat.AntiBot;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.Render.gl.GLUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Nametags
extends Module {
    private ArrayList<Entity> entities = new ArrayList();
    private int i = 0;
    Option<Boolean> mctheplayer = new Option<Boolean>("ShowYou", "ShowYou", true);
    public Nametags() {
        super("Nametags", ModuleType.Render);
        this.addSettings(mctheplayer);
    }
    @Override
    public void onEnable() {
        // TODO Auto-generated method stub
        super.onEnable();
    }
    @Override
    public void onDisable() {
        // TODO Auto-generated method stub
        super.onDisable();
    }
    @Runnable
    public void onRender(Event3D event) {
        for (Object o : mc.theWorld.playerEntities) {
            EntityPlayer p = (EntityPlayer) o;
            if(this.mctheplayer.value? p.isEntityAlive() : p.isEntityAlive() && p!=mc.thePlayer) {
                double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * Helper.getrenderPartialTicks()
                        - Helper.renderPosX();
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * Helper.getrenderPartialTicks()
                        - Helper.renderPosY();
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * Helper.getrenderPartialTicks()
                        - Helper.renderPosZ();
                renderNameTag(p, p.getName(), pX, pY, pZ);
            }


        }
    }

    private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        if( !entity.isInvisible()) {
            FontRenderer fr = mc.fontRendererObj;
            float size = mc.thePlayer.getDistanceToEntity(entity) / 10.0f;
            if(size < 1.1f) {
                size = 1.1f;
            }
            pY += (entity.isSneaking() ? 0.5D : 0.7D);
            float scale = size * 1.8f;
            scale /= 100f;
            tag = entity.getName();

            String bot = "";
            AntiBot ab = (AntiBot) Zelix.instance.getModuleManager().getModuleByClass(AntiBot.class);
            if(ab.invalidEntitys.contains(entity) && entity != mc.thePlayer) {
                bot = "\2479[Bot]";
            } else {
                bot = "";
            }

            String team = "";
            String lol = team + bot + tag;
            double plyHeal = entity.getHealth();
            String hp = "HP:" + (int)plyHeal;
            String diString =  (int)entity.getDistanceToEntity(mc.thePlayer) + "m ";
            GL11.glPushMatrix();
            GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 2.0F, 0.0F);
            GL11.glRotatef(mc.getRenderManager().playerViewX, 2.0F, 0.0F, 0.0F);
            GL11.glScalef(-scale, -scale, scale);
            GLUtil.setGLCap(2896, false);
            GLUtil.setGLCap(2929, false);
            int width = 31;
            float width2 =fr.getStringWidth(diString+lol)/2 + 2.2f;
            float nw = - width2 - 2.2f;
            int COLOR = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 60.0 + (double)50.0 / 30.0) % 1.0f,0.95f, 1.0f)).getRGB();
            GLUtil.setGLCap(3042, true);
            GL11.glBlendFunc(770, 771);
            RenderUtil.R2DUtils.drawRoundedRect(nw, -20.0f, width2, -8.0f,new Color(25,25,25,121).getRGB(),new Color(25,25,25,121).getRGB());
            RenderUtil.R2DUtils.drawRoundedRect(nw, -20.0f, nw+fr.getStringWidth(diString)+2, -8.0f,new Color(25,25,25,161).getRGB(),new Color(25,25,25,161).getRGB());
            RenderUtil.R2DUtils.drawRoundedRect(-width-0.4f, -8.4f, -width+61+0.4f, -3.6f,new Color(5,5,5,211).getRGB(),new Color(5,5,5,211).getRGB());
            RenderUtil.R2DUtils.drawRoundedRect(-width, -8.0f,(float) (-width+61*entity.getHealth()/entity.getMaxHealth()), -4.0f, entity == this.mc.thePlayer ?new Color(2,205,2,211).getRGB() : new Color(205,2,2,211).getRGB(),entity == this.mc.thePlayer ?new Color(2,205,2,211).getRGB() : new Color(205,2,2,211).getRGB());
            fr.drawString(diString+lol, (int) (nw+4.0f), (int)(-18f), new Color(255,255,255,154).getRGB());
            GL11.glScaled(0.6f, 0.6f, 0.6f);
            GL11.glScaled(1, 1, 1);
            int xLeft = (-width / 2 - 66);
            GL11.glPushMatrix();
            GL11.glScaled(1.2d, 1.2d, 1.2d);
            if (true) {
                int xOffset = 0;
                for (ItemStack armourStack : entity.inventory.armorInventory) {
                    if (armourStack != null) {
                        xOffset -= 11;
                    }
                }
                Object renderStack;
                if (entity.getHeldItem() != null) {
                    xOffset -= 8;
                    renderStack = entity.getHeldItem().copy();
                    if ((((ItemStack) renderStack).hasEffect())
                            && (((((ItemStack) renderStack).getItem() instanceof ItemTool))
                            || ((((ItemStack) renderStack).getItem() instanceof ItemArmor)))) {
                        ((ItemStack) renderStack).stackSize = 1;
                    }
                    renderItemStack((ItemStack) renderStack, xOffset, -50);
                    xOffset += 20;
                }
                for (ItemStack armourStack : entity.inventory.armorInventory) {
                    if (armourStack != null) {
                        ItemStack renderStack1 = armourStack.copy();
                        if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool))
                                || ((renderStack1.getItem() instanceof ItemArmor)))) {
                            renderStack1.stackSize = 1;
                        }
                        renderItemStack(renderStack1, xOffset, -50);
                        xOffset += 20;
                    }
                }
            }
            GL11.glPopMatrix();
            GLUtil.revertAllCaps();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }

    }

    public void renderItemStack(ItemStack stack, int x, int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        this.mc.getRenderItem().zLevel = -150.0F;
        whatTheFuckOpenGLThisFixesItemGlint();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
        this.mc.getRenderItem().zLevel = 0.0F;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    private void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public void drawBorderedRectNameTag(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        RenderUtil.drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

}
