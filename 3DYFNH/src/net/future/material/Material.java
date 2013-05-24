package net.future.material;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class Material 
{
	public Texture texture;
	public int id;
	public String name;
	public Vector3f ambient, diffuse, specular;
	public float shininess;
	public int shader;
}
