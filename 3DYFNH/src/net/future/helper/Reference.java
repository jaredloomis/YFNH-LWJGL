package net.future.helper;
import java.io.File;

import net.future.model.Model;
import net.future.model.ModelPoint;
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
	//public static String myVert = "res/shaders/MyTest.vert";
	//public static String myFrag = "res/shaders/MyTest.frag";
	public static Model player = new ModelPoint().setName("Player");
	public static Model bunny = OBJLoader.loadModel(new File("res/models/bunny.obj")).setShininess(128).setTexture("res/textures/BasicBlock.png").setShader(ShaderLoader.loadShaderPair(pixelVert, pixelFrag)).setName("Bunny").setUpAABB()/*.setScale(0.5f)*/.setUpVBO();
	public static Model cube = OBJLoader.loadModel(new File("res/models/square.obj")).setName("Cube");
}