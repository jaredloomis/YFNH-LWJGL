package net.future.model;
import java.util.ArrayList;
import java.util.List;


import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class Model 
{
	public Texture texture;
	public int shader = 0;
	public List<Vector3f> verts = new ArrayList<Vector3f>();
	public List<Vector3f> norms = new ArrayList<Vector3f>();
	public List<Face> faces = new ArrayList<Face>();
	public String name = "Default";
	public AABB boundingBox;
	public float scale = 1;
	public int vboVertexHandle;
	public int vboNormalHandle;
	public int vboTexHandle;

	public float shininess;

	public Model()
	{
		this.shininess = 0;
	}

	public Model setUpVBO() 
	{
		int[] vbos = OBJLoader.createVBO(this);
		this.vboVertexHandle = vbos[0];
		this.vboNormalHandle = vbos[1];
		this.vboTexHandle = vbos[2];

		return this;
	}

	public Model setTexture(String texturePath)
	{
		this.texture = MyTextureLoader.getTexture(texturePath);

		return this;
	}

	public Model setShader(int shaderID)
	{
		this.shader = shaderID;
		return this;
	}

	public Model setShininess(float shiny)
	{
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shiny);
		this.shininess = shiny;
		return this;
	}

	public Model setName(String newN)
	{
		this.name = newN;
		return this;
	}

	public Model setScale(float s)
	{
		this.scale = s;
		return this;
	}

	public Model setUpAABB()
	{
		for(int i = 0; i < this.faces.size(); i++)
		{
			Face f = this.faces.get(i);

			if(i==0)
			{
				this.boundingBox = f.boundingBox;
				//this.boundingBox.min = f.boundingBox.min;
			}
			else
			{
				this.boundingBox.max.x = Math.max(this.boundingBox.max.x, f.boundingBox.max.x);
				this.boundingBox.max.y = Math.max(this.boundingBox.max.y, f.boundingBox.max.y);
				this.boundingBox.max.z = Math.max(this.boundingBox.max.z, f.boundingBox.max.z);

				this.boundingBox.min.x = Math.min(this.boundingBox.min.x, f.boundingBox.min.x);
				this.boundingBox.min.y = Math.min(this.boundingBox.min.y, f.boundingBox.min.y);
				this.boundingBox.min.z = Math.min(this.boundingBox.min.z, f.boundingBox.min.z);
			}
		}

		return this;
	}
}
