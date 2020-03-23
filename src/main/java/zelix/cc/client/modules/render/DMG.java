package zelix.cc.client.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.render.Event3D;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Render.Mapping;
import zelix.cc.client.utils.Render.Mappings;
import zelix.cc.client.utils.Render.Particles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DMG
extends Module {
    public Mappings supreMap;
    public List<Particles> particlesList = new ArrayList<>();
    public DMG() {
        super("DMGPars", ModuleType.Render);
        supreMap = new Mappings();
    }
    @Runnable
    public void onEntityLoad(EventPPUpdate eventUpdate){
        if(eventUpdate.isPre())
            return;
        for(Entity entity : mc.theWorld.loadedEntityList){
            if(entity instanceof EntityLivingBase && !entity.isDead && !supreMap.contains((EntityLivingBase) entity)){
                supreMap.add(new Mapping(entity,((EntityLivingBase) entity).getHealth()));
            }
        }
        for(Mapping mapping : supreMap.getMappingList()){
            if(mapping.getHealth() != ((EntityLivingBase)mapping.getEntity()).getHealth())
            {
                double pX = mapping.getEntity().posX - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1;
                double pY = mapping.getEntity().getEntityBoundingBox().minY + (mapping.getEntity().getEntityBoundingBox().maxY - mapping.getEntity().getEntityBoundingBox().minY) / 2.0;
                double pZ = mapping.getEntity().posX
                        - 0.5 + new Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL ^ 0x203FF36645D9EA2FL)).nextInt(5) * 0.1;
                particlesList.add(new Particles(pX,pY,pZ,(mapping.getHealth() - ((EntityLivingBase) mapping.getEntity()).getHealth()),mapping.getEntity()));
                supreMap.add(new Mapping(mapping.getEntity(),((EntityLivingBase)mapping.getEntity()).getHealth()));
                supreMap.getMappingList().remove(mapping);
            }
        }
    }
    public void renderHP(Particles particles){
        EntityLivingBase entity = (EntityLivingBase) particles.entity;
        double pX = particles.renderPosX,pY = particles.renderPosY,pZ = particles.renderPosZ;
        float damage = particles.damage;
        if( !entity.isInvisible()) {
            double x = pX;
            double n = x - Helper.renderPosX();
            double y = pY;
            double n2 = y - Helper.renderPosY();
            double z = pZ;
            double n3 = z - Helper.renderPosZ();
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float)n, (float)n2, (float)n3);
            GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            float textY;
            if (this.mc.gameSettings.thirdPersonView == 2) {
                textY = -1.0f;
            }else {
                textY = 1.0f;
            }
            GlStateManager.rotate(this.mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
            final double size = 0.03;
            GlStateManager.scale(-size, -size, size);
            enableGL2D();
            disableGL2D();
            GL11.glDepthMask(false);
            String text;
            if (damage < 0.0f) {
                text = "\247a" + roundToPlace((damage) * -1.0f, 1);
            }else {
                text = "\247e" + roundToPlace(damage, 1);
            }
            this.mc.fontRendererObj.drawStringWithShadow(text, (float)(-(this.mc.fontRendererObj.getStringWidth(text) / 2)), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT - 1)), 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }
    public static double roundToPlace(double p_roundToPlace_0_,int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    @Runnable
    private void z3D(Event3D e){
        for(Particles particles : particlesList)
        {
            if(!particles.timerUtil.hasReached(2000)) {
                renderHP(particles);
            } else {
                particlesList.remove(particles);
            }
        }
    }
}
