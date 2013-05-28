package net.future.model;

import net.future.material.Material;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Face 
{
	public Material material;
	
	//These float arrays are not the actual points!
	//They are used to speed up the model loading process.
	//Use the VectorXf variables instead 
	//public float[] vertex;
	//public float[] normal;
	//public float[] texture;
	
	public Vector2f[] textureCoords;
	public Vector3f[] points;
	public Vector3f[] normals;
	
	public AABB boundingBox;
	
	/**
	 * Not reccomended to use this
	 * constructor
	 */
	public Face(Vector3f... points)
	{
		this.points = points;
	}
	
	public Face()
	{
		
	}
	
	public void setUpAABB()
	{
		this.boundingBox = new AABB(this);
	}
}
