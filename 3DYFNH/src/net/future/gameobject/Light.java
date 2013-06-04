package net.future.gameobject;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import net.future.world.World;

public class Light extends GameObject
{
	public FloatBuffer lightPosition;
	public FloatBuffer specInt; 
	public FloatBuffer ambientInt;
	public FloatBuffer diffInt;

	public float[] diffuse, pos, spec, ambient;
	
	public int id;

	public Light(World w, int id, float[] diffuse, float[] spec, float[] ambient) 
	{
		super(w);

		this.id = id;
		this.diffuse = diffuse;
		this.pos = new float[]{this.position.x, this.position.y, this.position.z, 0f};
		this.spec = spec;
		this.ambient = ambient;
	}
	
	@Override
	public void update()
	{
		//glPushAttrib(GL_TRANSFORM_BIT);
		//glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER, 1.0f);
		//glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER, 1.0f);

		//glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR);
		
		glLight(id, GL_POSITION, lightPosition);	
		glLightModel(GL_LIGHT_MODEL_AMBIENT, ambientInt);
		glLight(id, GL_AMBIENT, ambientInt);
		glLight(id, GL_SPECULAR, specInt);				
		glLight(id, GL_DIFFUSE, diffInt);
		//glPopAttrib();
	}
	
	public void init() 
	{
		initLightArrays();
		glLight(id, GL_POSITION, lightPosition);	
		glLightModel(GL_LIGHT_MODEL_AMBIENT, ambientInt);
		glLight(id, GL_AMBIENT, ambientInt);
		glLight(id, GL_SPECULAR, specInt);				
		glLight(id, GL_DIFFUSE, diffInt);
		
		glEnable(id);
		//glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		//glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	private void initLightArrays() 
	{
		diffInt = BufferUtils.createFloatBuffer(4);
		diffInt.put(diffuse[0]).put(diffuse[1]).put(diffuse[2]).put(diffuse[3]).flip();

		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(pos[0]).put(pos[1]).put(pos[2]).put(pos[3]).flip();

		specInt = BufferUtils.createFloatBuffer(4);
		specInt.put(spec[0]).put(spec[1]).put(spec[2]).put(spec[3]).flip();

		ambientInt = BufferUtils.createFloatBuffer(4);
		ambientInt.put(ambient[0]).put(ambient[1]).put(ambient[2]).put(ambient[3]).flip();
	}
}