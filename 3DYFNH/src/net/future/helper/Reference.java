package net.future.helper;
import java.io.File;
import net.future.model.Model;
import net.future.model.OBJLoader;
import net.future.model.ShaderLoader;

public class Reference 
{
	public static String shinyVert = "res/shaders/shinyVert.shader";
	public static String shinyFrag = "res/shaders/shinyFrag.shader";
	public static String testVert = "res/shaders/testVert.shader";
	public static String testFrag = "res/shaders/testFrag.shader";
	public static String pixelVert = "res/shaders/pixelVert.shader";
	public static String pixelFrag = "res/shaders/pixelFrag.shader";
	public static String textureVert = "res/shaders/texture.vert";
	public static String textureFrag = "res/shaders/texture.frag";
	public static String pixelTextureVert = "res/shaders/pixelTexture.vert";
	public static String pixelTextureFrag = "res/shaders/pixelTexture.frag";
	public static Model bunny = OBJLoader.loadModel(new File("res/models/wow/wow.obj")).setShininess(128).setShader(ShaderLoader.loadShaderPair(pixelTextureVert, pixelTextureFrag)).setName("Bunny").setUpAABB().setScale(1f).setUpVBO();
	public static Model test = OBJLoader.loadModel(new File("res/models/Ibanez/Ibanez.obj")).setShininess(128).setShader(ShaderLoader.loadShaderPair(pixelTextureVert, pixelTextureFrag)).setName("Bunny").setUpAABB().setScale(0.1f).setUpVBO();
}