package net.future.gameobject;
import java.io.File;
import net.future.model.Face;
import net.future.model.Model;
import net.future.model.OBJLoader;
import net.future.physpacks.IPhysPack;
import net.future.world.World;

import org.lwjgl.util.vector.Vector3f;

public class GameObject 
{
	public World world;
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f velocity;
	public String name = "Default";
	public IPhysPack physics;
	public boolean grounded;
	public float[] color;
	public Model model;
	public int list;

	/**
	 * Not Recomended.
	 * Used by <code>Player</code>
	 */
	public GameObject(World w)
	{
		this.model = new Model();
		this.model.faces.add(new Face(
				new Vector3f(0.05f, 0.2f, -0.05f),
				new Vector3f(0, -0.2f, 0),
				new Vector3f(-0.05f, 0, 0.05f)
				));
		this.model.faces.get(0).setUpAABB();
		this.model = this.model.setUpAABB();
		//this.model = null;
		this.world = w;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.velocity = new Vector3f(0, 0, 0);
	}
	
	public GameObject(World world, Model model)
	{
		this.world = world;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.velocity = new Vector3f(0, 0, 0);
		this.name = "Default";
		this.model = model;
	}
	
	public GameObject(World world, Model model, float[] color)
	{
		this.world = world;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.velocity = new Vector3f(0, 0, 0);
		this.name = "Default";
		this.color = color;
		this.model = model;
	}
	
	public GameObject(World world, String model, float[] color)
	{
		this.world = world;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.velocity = new Vector3f(0, 0, 0);
		this.name = "Default";
		this.color = color;
		this.model = OBJLoader.loadModel(new File(model));
	}
	
	public GameObject(GameObject copy)
	{
		this.world = copy.world;
		this.position = copy.position;
		this.rotation = copy.rotation;
		this.velocity = copy.velocity;
		this.name = copy.name;
		this.color = copy.color;
		this.model = copy.model;
		this.list = copy.list;
		
		//if(model != null)
		//	this.setUpList();
	}
	
	/**
	 * Called every update.
	 * Override if necessary
	 */
	public void update()
	{
		if(this.physics!=null)
			this.physics.update(this);
	}
	
	//public void setUpList()
	{
		//TODO uncomment this to enable display lists
		//this.list = OBJLoader.createDisplayList(this.model);
	}
	
	/**
	 * For Debugging Purposes
	 */
	public String getName()
	{
		return name;
	}
}
