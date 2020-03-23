package zelix.cc.client.manager;

import java.io.ByteArrayInputStream;

import net.utils.slick.opengl.Texture;
import net.utils.slick.opengl.TextureLoader;

public class TextureImage {

	public byte[] pixels;
	public Texture texture;
	public String location;
	
	public Texture getTexture() {
    	if (texture == null) {
    		if (pixels != null) {
    			try {
    				ByteArrayInputStream bias = new ByteArrayInputStream(pixels);
    				texture = TextureLoader.getTexture("PNG", bias);
    			} catch (Exception e) {
    			}
    		}
    	}
		return texture;
	}
}
