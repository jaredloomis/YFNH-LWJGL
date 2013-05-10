package net.future.gameobject;

import net.future.helper.Reference;
import net.future.world.World;

public class ObjectBunny extends GameObject
{	
	public ObjectBunny(World world)
	{
		super(world, Reference.bunny, new float[]{1f, 0f, 0f, 1.0f});
	}
}