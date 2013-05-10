package net.future.model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Face 
{
	public float[] vertex;
	public float[] normal;
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
	
	public Face(Vector3f vertex, Vector3f normal)
	{
		this.vertex = new float[]{vertex.x, vertex.y, vertex.z};
		this.normal = new float[]{normal.x, normal.y, normal.z};
	}
	
	public Face(float[] vertex, float[] normal)
	{
		this.vertex = vertex;
		this.normal = normal;
	}
	
	public void setUpAABB()
	{
		this.boundingBox = new AABB(this);
	}
}
