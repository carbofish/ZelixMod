package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.SideOnly;
import zelix.cc.client.modules.render.BlockEveryThing;
import zelix.cc.client.utils.BlockRender;

@Mixin(value= {ItemRenderer.class})
@SideOnly(value=Side.CLIENT)
public abstract class MixinItemRenderer {

	@Shadow
	private ItemStack itemToRender;
	
	@Shadow
	protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);
	
	@Shadow
	protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);
	
	@Shadow
	protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);
	
	@Shadow
	protected abstract void doBlockTransformations();
	
	@Shadow
	protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);
	
	@Shadow
	protected abstract void doItemUsedTransformations(float swingProgress);
	
	@Shadow
	protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);
	
	@Shadow
	protected abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);
	
	@Shadow
	protected abstract void rotateArroundXAndY(float angle, float angleY);
	
	@Shadow
	protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);
	
	@Shadow
	protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);
	
	@Shadow
	private float prevEquippedProgress;
	
	@Shadow
	private float equippedProgress;
	
	@Overwrite
	public void renderItemInFirstPerson(float partialTicks)
    {
		float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP)abstractclientplayer, partialTicks);
        float var4 = Minecraft.getMinecraft().thePlayer.getSwingProgress(partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null)
        {
            if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap)
            {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            }
            else if (abstractclientplayer.getItemInUseCount() > 0 || BlockRender.block || (BlockEveryThing.block && !(itemToRender.getItem() instanceof ItemFood) && !(itemToRender.getItem() instanceof ItemPotion) && !(itemToRender.getItem() instanceof ItemSword) && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()))
            {
            	if(BlockRender.block || (BlockEveryThing.block && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())) {
            		this.transformFirstPersonItem(f, 0.0F);
                	GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                    final float var9 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927f);
                    GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                    GlStateManager.rotate(-var9 * (float) 70.0 / 2.0f, -8.0f, -0.0f, 7.0f);
                    GlStateManager.rotate(-var9 * (float) 70.0, 1.5f, -0.4f, -0.0f);
            	}else {
            		EnumAction enumaction = this.itemToRender.getItemUseAction();

                    switch (enumaction)
                    {
                        case NONE:
                            this.transformFirstPersonItem(f, 0.0F);
                            break;
                        case EAT:
                        case DRINK:
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, 0.0F);
                            break;
                        case BLOCK:
                        	this.transformFirstPersonItem(f, 0.0F);
                        	GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                            GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                            final float var9 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927f);
                            GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                            GlStateManager.rotate(-var9 * (float) 70.0 / 2.0f, -8.0f, -0.0f, 7.0f);
                            GlStateManager.rotate(-var9 * (float) 70.0, 1.5f, -0.4f, -0.0f);
                            break;
                        case BOW:
                            this.transformFirstPersonItem(f, 0.0F);
                            this.doBowTransformations(partialTicks, abstractclientplayer);
                    }
            	}
                
            }
            else
            {
                this.doItemUsedTransformations(f1);
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible())
        {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
}
