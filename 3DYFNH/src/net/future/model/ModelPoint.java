package net.future.model;

import org.lwjgl.util.vector.Vector3f;

public class ModelPoint extends Model
{
	public ModelPoint()
	{
		this.verts.add(new Vector3f(0f, 0.1f, -0.1f));
		this.verts.add(new Vector3f(-0.1f, 0f, 0.1f));
		this.verts.add(new Vector3f(0f, 0f, 0f));
		
		this.norms.add(new Vector3f(0, 0, 0));
		this.norms.add(new Vector3f(0, 0, 0));
		this.norms.add(new Vector3f(0, 0, 0));
		
		this.norms.add(new Vector3f(0, 0, 0));
		Face f = new Face(verts.get(0), verts.get(1), verts.get(2));
		f.points[0] = this.verts.get(0);
		f.points[1] = this.verts.get(1);
		f.points[2] = this.verts.get(2);
		//f.vertex = new float[]{1, 2, 3};
		//f.normal = new float[]{1, 2, 3};
		this.faces.add(f);
	}
}