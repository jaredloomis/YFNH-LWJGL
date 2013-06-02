package net.future.model;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class Model 
{
	public List<Integer> temp = new ArrayList<Integer>();
	public Texture texture;
	public List<Texture> textures = new ArrayList<Texture>();
	public int shader = 0;
	public List<Vector3f> verts = new ArrayList<Vector3f>();
	public List<Vector3f> norms = new ArrayList<Vector3f>();
	public List<Vector2f> textureCoords = new ArrayList<Vector2f>();
	public List<Face> faces = new ArrayList<Face>();
	public String name = "Default";
	public AABB boundingBox;
	public float scale = 1;
	
	public int vboVertexHandle;
	public int vboNormalHandle;
	public int vboTexHandle;
	public int vboColorHandle;
	public int vboTexIDHandle;
	
	public FloatBuffer vertex;
	public FloatBuffer normal;
	public FloatBuffer text;
	public FloatBuffer color;
	public IntBuffer textID;

	public float shininess;

	public Model()
	{
		this.shininess = 0;
	}

	public Model setUpVBO() 
	{
		Object[][] vbos = OBJLoader.createVBO(this);
		
		this.vboVertexHandle = (int)vbos[0][0];
		this.vboNormalHandle = (int)vbos[1][0];
		this.vboTexHandle = (int)vbos[2][0];
		this.vboColorHandle = (int)vbos[3][0];
		this.vboTexIDHandle = (int)vbos[4][0];
		
		this.vertex = (FloatBuffer)vbos[0][1];
		this.normal = (FloatBuffer)vbos[1][1];
		this.text = (FloatBuffer)vbos[2][1];
		this.color = (FloatBuffer)vbos[3][1];
		this.textID = (IntBuffer)vbos[4][1];

		return this;
	}

	public void setDefaultTextCoords()
	{
		for(int i = 0; i < this.faces.size(); i++)
		{
			Face f = this.faces.get(i);

			f.textureCoords = new Vector2f[]
					{
					new Vector2f(0, 0),
					new Vector2f(1, 0),
					new Vector2f(1, 1)
					};
		}
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
		//Not neccessary, could remove...
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shiny);
		this.shininess = shiny;
		return this;
	}

	public Model setName(String newN)
	{
		this.name = newN;
		return this;
	}
	
	/**
	 * Just setting the scale variable of the model
	 * makes the collision detection and width, height
	 * of the model completely wrong, so I just scale each of the 
	 * point's positions to the specified scale
	 */
	public Model setScale(float s)
	{
		if(s==0||s==1)
			return this;
		//this.scale = s;
		for(int i = 0; i < this.faces.size(); i++)
		{
			for(int j = 0; j < this.faces.get(i).points.length; j++)
			{
				//Update position of each point
				Vector3f cur = this.faces.get(i).points[j];
				this.faces.get(i).points[j] = new Vector3f(cur.x*s, cur.y*s, cur.z*s);
			}
			
			//Update AABB of face
			Face f = this.faces.get(i);
			Vector3f min = new Vector3f(f.boundingBox.min.x*s, f.boundingBox.min.y*s, f.boundingBox.min.z*s);
			Vector3f max = new Vector3f(f.boundingBox.max.x*s, f.boundingBox.max.y*s, f.boundingBox.max.z*s);
			f.boundingBox = new AABB(min, max);
		}
		//Update AABB of model
		Vector3f min = new Vector3f(this.boundingBox.min.x*s, this.boundingBox.min.y*s, this.boundingBox.min.z*s);
		Vector3f max = new Vector3f(this.boundingBox.max.x*s, this.boundingBox.max.y*s, this.boundingBox.max.z*s);
		this.boundingBox = new AABB(min, max);
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
				//this.boundingBox.min=f.boundingBox.min;
				//this.boundingBox.max = f.boundingBox.max;
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
