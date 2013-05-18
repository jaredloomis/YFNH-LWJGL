package net.future.world;
import java.util.ArrayList;
import java.util.List;
import net.future.gameobject.GameObject;
import net.future.gameobject.Light;
import net.future.gameobject.ObjectBunny;
import net.future.math.GeometryHelper;
import net.future.model.Model;
import net.future.player.Player;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class World 
{
	public List<GameObject> objects;
	public Light[] lights;
	public GameObject cube;
	public boolean paused;

	public World()
	{
		this.objects = new ArrayList<GameObject>();
		this.lights = new Light[6];
		this.paused = false;

		cube = new ObjectBunny(this);
		cube.position = new Vector3f(0, -10, 0);
		cube.name = "bunny";
		this.add(cube);

		Light l1 = new Light(this, GL11.GL_LIGHT0, new float[]{0.5f,0.5f,0.5f,1}, new float[]{0.5f,0.5f,0.5f,1}, new float[]{0.05f, 0.05f, 0.05f, 1});
		l1.init();
		this.lights[0] = l1;
	}

	/**
	 * Main method of the game.
	 * Moves and draws all objects.
	 */
	public void update()
	{
		for(int i = 0; i < objects.size(); i++)
		{
			GameObject cur = this.objects.get(i);

			glPushMatrix();
			{
				if(cur.model != null)
				{
					//Moves the obj
					if(cur instanceof Player)
						;//glTranslatef(cur.position.x, cur.position.y, cur.position.z);
					else
					{
						//WHAT WAY TO RENDER
						int renderType = 1;

						//If not using VBOs, use display lists
						if(renderType==0)
						{
							Model m = cur.model;

							//Move the object
							glTranslatef(-cur.position.x, -cur.position.y, -cur.position.z);

							//Scale the object using the scale variable
							glScalef(cur.model.scale, cur.model.scale, cur.model.scale);

							//Set the shininess
							glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);

							//Set the color
							glColor3f(cur.color[0], cur.color[1], cur.color[2]);

							//Use the model's shader
							glUseProgram(m.shader);

							//Bind the current model's texture
							glBindTexture(GL_TEXTURE_2D, m.texture.getTextureID());
							m.texture.bind();

							//Old way to call objects
							glCallList(cur.list);
						}
						//If using VBOs
						else if(renderType==1)
						{
							Model m = cur.model;

							//Move the object
							//TODO Make all negative again? I don't think so but...
							glTranslatef(cur.position.x, cur.position.y, cur.position.z);

							//Scale the object using the scale variable
							//glScalef(cur.model.scale, cur.model.scale, cur.model.scale);

							//Set the shininess
							glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);

							//Set the color
							glColor3f(cur.color[0], cur.color[1], cur.color[2]);

							//Use the model's shader
							glUseProgram(m.shader);

							if(m.texture!=null)
							{
								GL13.glActiveTexture(GL13.GL_TEXTURE0);
								
								glBindTexture(GL_TEXTURE_2D, m.texture.getTextureID());
								//Bind it again because... idk, its not working!
								m.texture.bind();
								
								//Find the "memory address" of texture_diffuse uniform in shader
								int loc = glGetUniformLocation(m.shader, "texture1");
								
								//Pass the 0 value to the sampler meaning it is to use texture unit 0.
								glUniform1i(loc, 0);
							}

							//// Set Data for Vertices ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboVertexHandle);
							glBufferData(GL_ARRAY_BUFFER, m.vertex, GL_STATIC_DRAW);
							glVertexPointer(3, GL_FLOAT, 0, 0L);

							//// Set Data for Normals ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboNormalHandle);
							glBufferData(GL_ARRAY_BUFFER, m.normal, GL_STATIC_DRAW);
							glNormalPointer(GL_FLOAT, 0, 0L);

							//// Set Data for Texture Coordinates ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboTexHandle);
							glBufferData(GL_ARRAY_BUFFER, m.text, GL_STATIC_DRAW);
							glTexCoordPointer(2, GL_FLOAT, 0, 0L);

							//Unbind Buffer for VBO
							glBindBuffer(GL_ARRAY_BUFFER, 0);

							//"Turn on" All Necessary client states
							glEnableClientState(GL_VERTEX_ARRAY);
							glEnableClientState(GL_TEXTURE_COORD_ARRAY);
							glEnableClientState(GL_NORMAL_ARRAY);
							{

								//Actually Draw the object
								glDrawArrays(GL_TRIANGLES, 0, m.faces.size() * 3);

							}
							//"Turn off" All Necessary client states
							glDisableClientState(GL_VERTEX_ARRAY);
							glDisableClientState(GL_TEXTURE_COORD_ARRAY);
							glDisableClientState(GL_NORMAL_ARRAY);
						}

						//Resets the array buffer, shader, texture, and color
						glBindBuffer(GL_ARRAY_BUFFER, 0);
						glUseProgram(0);
						cur.model.texture.release();
						glBindTexture(GL_TEXTURE_2D, 0);
						glColor4f(1, 1, 1, 1);

						glLoadIdentity();
					}
				}
			}
			glPopMatrix();

			//If game is paused do not perform any logic
			if(!paused)
				cur.update();
		}

		for(int i =0; i < this.lights.length; i++)
		{
			if(this.lights[i]!=null)
				this.lights[i].update();
		}

		Display.update();
	}

	/**
	 * Safely adds object
	 */
	public void add(GameObject obj)
	{
		if(canPlaceHere(obj, (int)obj.position.x, (int)obj.position.y, (int)obj.position.z))
		{
			this.objects.add(obj);
		}
	}

	/**
	 * Safely moves objects by checking
	 * if canPlaceHere
	 */
	public boolean moveObj(GameObject obj, Vector3f pos)
	{
		Vector3f initialPosition = new Vector3f(obj.position.x, obj.position.y, obj.position.z);

		//Placing the object first and then testing to see if it
		//is allowed to be there seems to be faster.
		obj.position = pos;
		if(!canPlaceHere(obj, pos.x, pos.y, pos.z))
		{
			obj.position = initialPosition;
			return false;
		}
		return true;
	}

	/**
	 * Makes sure Object does not
	 * collide with other objects
	 */
	public boolean canPlaceHere(GameObject obj, float x, float y, float z)
	{
		//if(obj.model != null)
		{
			for(int i = 0; i < this.objects.size(); i++)
			{
				GameObject cur = this.objects.get(i);

				if(cur != obj)
				{
					if(GeometryHelper.willIntersect(obj, cur, new Vector3f(x, y, z)))
					{
						return false;
					}

				}
			}
		}

		return true;
	}
}

