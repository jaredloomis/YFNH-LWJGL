package net.future.physpacks;
import org.lwjgl.util.vector.Vector3f;
import net.future.gameobject.GameObject;
import net.future.player.Player;

public class PhysPlayer implements IPhysPack
{
	//public boolean landed = false;
	public float decay=0.01f;
	public float gravity = 0.005f;

	@Override
	public void update(GameObject o) 
	{
		Vector3f v = o.velocity;
		this.velocity(o, v);
	}

	public void velocity(GameObject o, Vector3f v)
	{
		if(((Player)o).cam.fly)
		{
			//If at least one of the x, y, z velocity variables 
			//has a value other than 0
			if(Math.abs(v.x)+Math.abs(v.y)+Math.abs(v.z)>0)
			{
				//o.world.moveObj(o, new Vector3f(o.position.x+v.x*(1-decay), o.position.y+v.y*(1-decay), o.position.z+v.z*(1-decay)));
				o.world.moveObj(o, new Vector3f(o.position.x+v.x*(1-decay), o.position.y, o.position.z));
				o.world.moveObj(o, new Vector3f(o.position.x, o.position.y+v.y*(1-decay), o.position.z));
				o.world.moveObj(o, new Vector3f(o.position.x, o.position.y, o.position.z+v.z*(1-decay)));

				o.velocity = new Vector3f(v.x*decay, v.y*decay, v.z*decay);
			}
		}
		else
		{
			//If at least one of the x, y, z velocity variables 
			//has a value other than 0
			if(Math.abs(v.x)+Math.abs(v.y)+Math.abs(v.z)>0)
			{
				o.world.moveObj(o, new Vector3f(o.position.x+v.x*(1-decay), o.position.y, o.position.z));

				if(!o.world.moveObj(o, new Vector3f(o.position.x, o.position.y+v.y*(1-decay), o.position.z)))
					o.grounded = true;
				else
					o.grounded = false;

				o.world.moveObj(o, new Vector3f(o.position.x, o.position.y, o.position.z+v.z*(1-decay)));
				
				o.velocity = new Vector3f(v.x*decay, v.y, v.z*decay);
			}
			if(!o.grounded)
				o.velocity.y-=gravity;
		}
	}
}
