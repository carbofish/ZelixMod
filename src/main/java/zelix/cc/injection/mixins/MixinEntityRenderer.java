package zelix.cc.injection.mixins;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.events.render.Event3D;
import zelix.cc.injection.interfaces.IMixinEntityRenderer;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

	@Shadow
	private float farPlaneDistance;
	
	@Shadow
	protected abstract void setupCameraTransform(float partialTicks, int pass);
	
	@Shadow
	private boolean debugView = false;
	
	@Shadow
	private int rendererUpdateCount;
	
	@Override
    public void setupCameraTransformwith(float partialTicks, int pass){
        this.setupCameraTransformwith(partialTicks, pass);
    }
	
	@Override
	public void orientCameraWith(float partialTicks) {
		this.orientCamera(partialTicks);
	}
	
	@Shadow
	protected abstract void orientCamera(float partialTicks);
	
	@Inject(method="renderWorldPass", at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift=At.Shift.BEFORE)})
	private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
		EventController.call(new Event3D(partialTicks));
	}

	//public static Minecraft mc = Minecraft.getMinecraft();
	
	/*public void setupCameraTransformExt(float partialTicks, int pass)
    {
    	this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
    	
    	if (mc.gameSettings.isFogFancy())
    	{
    		this.farPlaneDistance *= 0.95F;
    	}
    	
    	if (Config.isFogFast())
    	{
    		this.farPlaneDistance *= 0.83F;
    	}
    	
    	GlStateManager.matrixMode(5889);
    	GlStateManager.loadIdentity();
    	float var3 = 0.07F;
    	
    	if (this.mc.gameSettings.anaglyph)
    	{
    		GlStateManager.translate((-(pass * 2 - 1)) * var3, 0.0F, 0.0F);
    	}
    	
    	this.clipDistance = this.farPlaneDistance * 2.0F;
    	
    	if (this.clipDistance < 173.0F)
    	{
    		this.clipDistance = 173.0F;
    	}
    	
    	if (this.mc.theWorld.provider.getDimensionId() == 1)
    	{
    		this.clipDistance = 256.0F;
    	}
    	
    	if (this.cameraZoom != 1.0D)
    	{
    		GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
    		GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
    	}
    	
    	Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, 100000f);
    	GlStateManager.matrixMode(5888);
    	GlStateManager.loadIdentity();
    	
    	if (this.mc.gameSettings.anaglyph)
    	{
    		GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
    	}
    	
    	this.hurtCameraEffect(partialTicks);
    	
    	if (this.mc.gameSettings.viewBobbing)
    	{
    		this.setupViewBobbing(partialTicks);
    	}
    	
    	
    	float var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
    	
    	if (var4 > 0.0F)
    	{
    		byte var5 = 20;
    		
    		if (this.mc.thePlayer.isPotionActive(Potion.confusion))
    		{
    			var5 = 7;
    		}
    		
    		float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
    		var6 *= var6;
    		GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * var5, 0.0F, 1.0F, 1.0F);
    		GlStateManager.scale(1.0F / var6, 1.0F, 1.0F);
    		GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * var5, 0.0F, 1.0F, 1.0F);
    	}
    	
    	this.orientCamera(partialTicks);
    	
    	if (this.debugView)
    	{
    		switch (this.debugViewDirection)
    		{
    		case 0:
    			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
    			break;
    			
    		case 1:
    			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    			break;
    			
    		case 2:
    			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
    			break;
    			
    		case 3:
    			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
    			break;
    			
    		case 4:
    			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
    		}
    	}
    }*/
}
