package zelix.cc.client.inGameGui.newclickgui;

import scala.xml.dtd.impl.Base;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class Position {
	
	public String text;
	public Module mod;
	public ModuleType type;
	public float x;
	public float y;
	public float weight;
	public Base.Alt alt;
	public float height;
	public String PositionType;
	public Value v;
	
	public Position(float x,float y,float weight,float height,String customtext,Module mod,ModuleType type) {
		this.x = x;
		this.y = y;
		this.text = customtext;
		this.type = type;
		this.mod = mod;
		this.weight = weight;
		this.height = height;
		this.PositionType = "Both";
	}
	
	public Position(float x,float y,float weight,float height,Module mod) {
		this.x = x;
		this.y = y;
		this.mod = mod;
		this.weight = weight;
		this.height = height;
		this.PositionType = "Module";
	}
	
	public Position(float x,float y,float weight,float height,String customtext) {
		this.x = x;
		this.y = y;
		this.text = customtext;
		this.PositionType = "Text";
	}
	
	public Position(float x,float y,float weight,float height,ModuleType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.weight = weight;
		this.height = height;
		this.PositionType = "ModuleCategory";
	}
	
	public Position(float x, float y, float weight, float height, Base.Alt at, String tp) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.weight = weight;
		this.height = height;
		this.PositionType = tp;
	}
	
	public Position(float x,float y,float weight,float height,Module mod,Value v) {
		this.x = x;
		this.y = y;
		this.v = v;
		this.weight = weight;
		this.height = height;
		this.mod = mod;
		this.PositionType = "Value";
	}
	
	public boolean istouch(int x,int y) {
		if(x >= this.x && y >= this.y && x <= (this.x+this.weight) && y <= (this.y+this.height)) {
			return true;
		}else {
			return false;
		}
	}
	
}
