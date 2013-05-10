package net.future.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
}