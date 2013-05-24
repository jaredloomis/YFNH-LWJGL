package net.future.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class MyTextureLoader 
{
	public static Texture getTexture(String location)
	{
		Texture texture = null;
		try 
		{
			if(location.endsWith(".png"))
				texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(location)));
			else if(location.endsWith(".jpg") || location.endsWith(".jpeg"))
				texture = TextureLoader.getTexture("JPG", new FileInputStream(new File(location)));
			else
				System.err.println("Texture File At " + location + " is not a supported image type");
			return texture;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		return null;
	}

	public static void render(Texture text, float width, float height)
	{
		text.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(width, 0, height);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-width,0, height);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(-width,0,-height);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(width,0,-height);
		GL11.glTexCoord2f(0, 1);
		GL11.glEnd();
	}

	public static int loadTexture(Texture t)
	{
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();

		try
		{
			ByteBuffer data = ByteBuffer.allocateDirect((int)(4*t.getWidth()*t.getHeight()));
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA, (int)t.getWidth(), (int)t.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
			
			tmp.rewind();
			return tmp.get(0);
		}
		catch(Exception e){e.printStackTrace();}
		return 0;
	}
}