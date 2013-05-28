package net.future.model;

import org.lwjgl.util.vector.Vector3f;

public class AABB
{
	public Vector3f min;
	public Vector3f max;

	public AABB(Face f)
	{
		for(int i = 0; i < f.points.length; i++)
		{
			Vector3f point = f.points[i];

			if(i==0)
			{
				this.min = new Vector3f(point.x, point.y, point.z);
				this.max = new Vector3f(point.x, point.y, point.z);
			}
			else
			{
				this.min = new Vector3f(
						Math.min(this.min.x, point.x),
						Math.min(this.min.y, point.y),
						Math.min(this.min.z, point.z)
						);

				this.max = new Vector3f(
						Math.max(this.max.x, point.x),
						Math.max(this.max.y, point.y),
						Math.max(this.max.z, point.z)
						);
			}
		}
	}
	
	/**
	 * Get length, width and height of AABB
	 */
	public Vector3f getDimensions()
	{
		return Vector3f.sub(this.max, this.min, null);
	}
	
	public AABB(AABB box, Vector3f offset)
	{
		this.min=Vector3f.add(box.min, offset, null);
		this.max=Vector3f.add(box.max, offset, null);
	}

	public AABB(Vector3f min, Vector3f max)
	{
		this.min = min;
		this.max = max;
	}

	public boolean intersecting(AABB b)
	{	
		return(this.max.x > b.min.x && 
			   this.min.x < b.max.x &&
			   this.max.y > b.min.y &&
			   this.min.y < b.max.y &&
			   this.max.z > b.min.z &&
			   this.min.z < b.max.z);
	}

	public static AABB getBox(float length, float width, float height)
	{
		if(length>=0&&height>=0&&height>=0)
			return new AABB(new Vector3f(0, 0, 0), new Vector3f(length, width, height));
		return null;
	}
	
	@Override
	public String toString()
	{
		return "AABB(Min("+this.min+"), Max("+this.max+"))";
	}
}
