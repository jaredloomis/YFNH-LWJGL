package net.future.gameobject;
import java.io.File;

import net.future.model.OBJLoader;
import net.future.world.World;

public class ObjectCube extends GameObject
{
	public ObjectCube(World world)
	{
		super(world, OBJLoader.loadModel(new File("res/models/cubeTest.obj")), new float[]{1f, 0f, 0.4f, 1.0f});
	}
}
