package net.future.helper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.future.model.Face;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class GeneralTools 
{
	public static Vector3f calculateSurfaceNormalsAgain(Face face)
	{
		Vector3f normal = new Vector3f(0, 0, 0);
		for(int i = 0; i < face.points.length-1; i++)
		{
			normal = Vector3f.cross(face.points[i], face.points[i+1], null);
		}
		return normal;
	}
	
	public static Texture loadTexture(File f)
	{
		String extension = f.getName().substring(f.getName().length()-3, f.getName().length());
		
		try 
		{
			return TextureLoader.getTexture(extension, new FileInputStream(f));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
