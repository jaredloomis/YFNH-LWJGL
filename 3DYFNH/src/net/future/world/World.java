package net.future.world;
import java.util.ArrayList;
import java.util.List;
import net.future.gameobject.GameObject;
import net.future.gameobject.Light;
import net.future.gameobject.ObjectBunny;
import net.future.helper.Reference;
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
		cube.position = new Vector3f(0, 0, 0);
		cube.name = "bunny";
		this.add(cube);
		
		GameObject test = new GameObject(this, Reference.test);
		test.position = new Vector3f(3, 0, 0);
		test.name = "test";
		this.add(test);

		Light l1 = new Light(this, GL11.GL_LIGHT0, new float[]{0.5f,0.5f,0.5f,1}, new float[]{0.5f,0.5f,0.5f,1}, new float[]{0.01f, 0.01f, 0.01f, 1});
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

						//renderType 0 means use display lists (DEPRECATED) NOT EVEN POSSIBLE ANYMORE
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

							//Old way to call objects
							glCallList(cur.list);
						}
						//renderType 1 means use VBOs (Faster, better, cooler)
						else if(renderType==1)
						{	
							Model m = cur.model;

							//Move the object
							glTranslatef(cur.position.x, cur.position.y, cur.position.z);

							//Set the shininess
							glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);

							//Set the color
							//glColor3f(cur.color[0], cur.color[1], cur.color[2]);

							//Use the model's shader
							glUseProgram(m.shader);


							int texIDVar = -1;

							if(m.texture!=null)
							{
								//OpenGL can't handle more than 7 GL_TEXTUREX s usiong this method
								if(m.textures.size()<7)
									for(int j = 0; j < m.textures.size(); j++)
									{
										GL13.glActiveTexture(GL13.GL_TEXTURE0+j);
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, m.textures.get(j).getTextureID());
										//m.textures.get(j).bind();
									}

								//Set the shader's "textures[10]" array equal to {0, 1, 2, 3, 4, ect.}
								//Cannot set as constant in shader because sampler2D s must be set by
								//Java code as a Uniform
								for(int j = 0; j < m.textures.size(); j++)
								{
									//Find the "memory address" of textures sampler2D's specified index uniform in shader
									int loc2 = glGetUniformLocation(m.shader, "textures["+j+"]");
									
									//Set it to current number
									glUniform1i(loc2, j);
								}

								//System.out.println(m.textID.position() + "---" + m.textID.get());

								//Find the "memory address" of textureID attribute in shader
								texIDVar = glGetAttribLocation(m.shader, "textureID");

								//// Set Data for Texture IDs ////
								glBindBuffer(GL_ARRAY_BUFFER, m.vboTexIDHandle);
								glVertexAttribPointer(texIDVar, 1, GL_FLOAT, false, 0, 0);

								//Enable the VBO to use this attribute
								glEnableVertexAttribArray(texIDVar);

								//// Set Data for Texture Coordinates ////
								glBindBuffer(GL_ARRAY_BUFFER, m.vboTexHandle);
								glTexCoordPointer(2, GL_FLOAT, 0, 0L);

								//Enable the VBO to use texture coords
								glEnableClientState(GL_TEXTURE_COORD_ARRAY);
							}

							//// Set Data for Vertices ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboVertexHandle);
							glVertexPointer(3, GL_FLOAT, 0, 0L);

							//// Set Data for Normals ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboNormalHandle);
							glNormalPointer(GL_FLOAT, 0, 0L);

							//// Set Data For Color
							glBindBuffer(GL_ARRAY_BUFFER, m.vboColorHandle);
							glColorPointer(3, GL_FLOAT, 0, 0L);

							//Unbind GL_ARRAY_BUFFER
							glBindBuffer(GL_ARRAY_BUFFER, 0);

							//"Turn on" All Necessary client states
							glEnableClientState(GL_VERTEX_ARRAY);
							glEnableClientState(GL_COLOR_ARRAY);
							glEnableClientState(GL_NORMAL_ARRAY);
							{

								//Actually Draw the object
								glDrawArrays(GL_TRIANGLES, 0, m.faces.size() * 3);

							}
							//"Turn off" All Necessary client states
							glDisableClientState(GL_VERTEX_ARRAY);
							if(m.texture!=null)
							{
								glDisableClientState(GL_TEXTURE_COORD_ARRAY);
								glDisableVertexAttribArray(texIDVar);
							}
							glDisableClientState(GL_COLOR_ARRAY);
							glDisableClientState(GL_NORMAL_ARRAY);
							
							//Unbind all textures
							if(m.textures.size()<7)
								for(int j = 0; j < m.textures.size(); j++)
								{
									GL13.glActiveTexture(GL13.GL_TEXTURE0+j);
									GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
									//m.textures.get(j).bind();
								}
						}

						//Resets the array buffer, shader, texture, and color to default values
						glBindBuffer(GL_ARRAY_BUFFER, 0);
						glUseProgram(0);
						glBindTexture(GL_TEXTURE_2D, 0);
						glColor4f(1, 1, 1, 1);

						glLoadIdentity();
					}
				}
			}
			glPopMatrix();

			//If game is paused do not perform any logic updates
			if(!paused)
				cur.update();
		}
		
		//Call the update method for all lights in scene
		for(int i =0; i < this.lights.length; i++)
		{
			if(this.lights[i]!=null)
				this.lights[i].update();
		}

		//Sync the GPU with the CPU, basically required
		Display.update();

		//TODO I should enable this, but it feels alot more jittery when I do
		//Display.sync(80);
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

