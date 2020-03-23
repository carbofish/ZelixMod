package zelix.cc.client.inGameGui.newclickgui.draw;

import java.io.IOException;
import java.io.InputStream;


import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TextureManager {
	/*notification*/
	public ResourceLocation notifacition_success;
	public ResourceLocation notifacition_error;
	public ResourceLocation notifacition_info;
	
	/*clickgui*/
	public ResourceLocation clickgui_background;
	public ResourceLocation module_clicked;
	public ResourceLocation module_list_background;
	public ResourceLocation module_setting_background;
	
	public ResourceLocation modebuttleUp;
	public ResourceLocation modebuttleDown;
	public ResourceLocation mode_value_background;
	public ResourceLocation mode_value_background_open;
	
	public ResourceLocation clickgui_combat;
	public ResourceLocation clickgui_world;
	public ResourceLocation clickgui_render;
	public ResourceLocation clickgui_player;
	public ResourceLocation clickgui_movement;
	
	public ResourceLocation module_toggle_open;
	public ResourceLocation module_toggle_close;
	
	public ResourceLocation boolean_open;
	public ResourceLocation boolean_close;
	
	public ResourceLocation modulelist_show_open;
	public ResourceLocation modulelist_show_close;
	
	public ResourceLocation number_value_background;
	public ResourceLocation number_value_chose;
	public ResourceLocation number_value_choser;
	
	public void load() throws IOException {
		notifacition_success = new ResourceLocation("Eliru/notification/success.png");
		notifacition_error = new ResourceLocation("Eliru/notification/warn.png");
		notifacition_info = new ResourceLocation("Eliru/notification/info.png");
		
		clickgui_background = new ResourceLocation("Eliru/clickgui/clickgui_background.png");
		
		modebuttleUp = new ResourceLocation("Eliru/clickgui/chevron-up.png");
		modebuttleDown = new ResourceLocation("Eliru/clickgui/chevron-down.png");
		
		clickgui_combat = new ResourceLocation("Eliru/clickgui/combat.png");
		clickgui_render = new ResourceLocation("Eliru/clickgui/eye.png");
		clickgui_world = new ResourceLocation("Eliru/clickgui/earth.png");
		clickgui_movement = new ResourceLocation("Eliru/clickgui/walk.png");
		clickgui_player = new ResourceLocation("Eliru/clickgui/player.png");
		
		modulelist_show_open = new ResourceLocation("Eliru/clickgui/checkbox-blank-circle.png");
		modulelist_show_close = new ResourceLocation("Eliru/clickgui/checkbox-blank-circle-outline.png");
		
		boolean_open = new ResourceLocation("Eliru/clickgui/checkbox-marked.png");
		boolean_close = new ResourceLocation("Eliru/clickgui/checkbox-blank-outline.png");
	
		module_toggle_open = new ResourceLocation("Eliru/clickgui/toggle-switch.png");
		module_toggle_close = new ResourceLocation("Eliru/clickgui/toggle-switch-off.png");
		
		mode_value_background = new ResourceLocation("Eliru/clickgui/Mode_Value_Background.png");
		mode_value_background_open = new ResourceLocation("Eliru/clickgui/Mode_Value_Clicked.png");
		
		module_clicked  = new ResourceLocation("Eliru/clickgui/Module_Clicked.png");
		module_list_background  = new ResourceLocation("Eliru/clickgui/Module_List_Background.png");
		module_setting_background  = new ResourceLocation("Eliru/clickgui/Module_Setting_Background.png");
	
		number_value_background = new ResourceLocation("Eliru/clickgui/Number_Value_Background.png");
		number_value_chose = new ResourceLocation("Eliru/clickgui/Number_Value_Chose.png");
		number_value_choser = new ResourceLocation("Eliru/clickgui/Number_Value_Chose.png");
	}
	
	public InputStream resToInput(ResourceLocation res) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
	}
}
