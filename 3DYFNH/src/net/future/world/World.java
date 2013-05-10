package net.future.world;
import java.util.ArrayList;
import java.util.List;
import net.future.gameobject.GameObject;
import net.future.gameobject.Light;
import net.future.gameobject.ObjectBunny;
import net.future.model.Model;
import net.future.model.MyTextureLoader;
import net.future.player.Player;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
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
		cube.position = new Vector3f(5, 5, 5);
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
		//Draws all objects on the screen
		//glCallList(cur.model.list);
		for(int i = 0; i < objects.size(); i++)
		{
			GameObject cur = this.objects.get(i);

			glPushMatrix();
			{
				MyTextureLoader.getTexture("res/textures/BasicBlock.png").bind();
				//QUAD
				glBegin(GL_QUADS);
				{
					glTexCoord2f(0, 0);
					glColor3f(1, 0, 0);
					glVertex3f(0, 0, 0);

					glTexCoord2f(1, 0);
					glColor3f(0, 1, 0);
					glVertex3f(1, 0, 0);

					glTexCoord2f(1, 1);
					glColor3f(1, 0, 1);
					glVertex3f(1, 0, 1);

					glTexCoord2f(0, 1);
					glColor3f(0, 0, 1);
					glVertex3f(0, 0, 1);
				}
				glEnd();


				if(cur.model != null)
				{	
					//Moves the obj
					if(cur instanceof Player)
						;//glTranslatef(cur.position.x, cur.position.y, cur.position.z);
					else
					{
						//Old way to call objects
						//glCallList(cur.list);
						
						Model m = cur.model;
						
						//int diffuseUniform = glGetUniformLocation(m.shader, "");
						//glUniform1f();

						//Move the object
						glTranslatef(-cur.position.x, -cur.position.y, -cur.position.z);

						//Scale the object using the scale variable
						glScalef(cur.model.scale, cur.model.scale, cur.model.scale);

						//Set the shininess
						glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);
						
						glColor3f(cur.color[0], cur.color[1], cur.color[2]);
						
						//Use the model's shader
						glUseProgram(m.shader);

						//Make GL_ARRAY_BUFFER reference the model's
						//vertex handle
						glBindBuffer(GL_ARRAY_BUFFER, m.vboVertexHandle);

						//Set the current vbo vertex array to whatever is in
						//GL_ARRAY_BUFFER.
						//Params: size, type, 0, offset
						glVertexPointer(3, GL_FLOAT, 0, 0L);

						//Make GL_ARRAY_BUFFER reference the model's
						//normal handle
						glBindBuffer(GL_ARRAY_BUFFER, m.vboNormalHandle);

						//Set the current vbo normal array to whatever is in
						//GL_ARRAY_BUFFER.
						//Params: size, type, 0, offset
						glNormalPointer(GL_FLOAT, 0, 0L);

						//Bind the model's texture
						cur.model.texture.bind();

						//Make GL_ARRAY_BUFFER reference the model's
						//Texture Coordinate handle
						glBindBuffer(GL_ARRAY_BUFFER, m.vboTexHandle);

						//Params: size, type, 0, offset
						glTexCoordPointer(3, GL_FLOAT, 0, 0L);

						//Allow VBOs
						glEnableClientState(GL_VERTEX_ARRAY);
						glEnableClientState(GL_NORMAL_ARRAY);
						glEnableClientState(GL_TEXTURE_COORD_ARRAY);

						
						//Set the color
						glColor3f(cur.color[0], cur.color[1], cur.color[2]);

						//TODO Remove?
						//Sets specular to the lights[0] object's specularity
						//glMaterial(GL_FRONT, GL_SPECULAR, m.s);

						//Draw the model
						//Params: RenderMode, first index to render, last index to render
						glDrawArrays(GL_TRIANGLES, 0, m.faces.size() * 3);

						//Disable the VBOS
						glDisableClientState(GL_VERTEX_ARRAY);
						glDisableClientState(GL_NORMAL_ARRAY);
						glDisableClientState(GL_TEXTURE_COORD_ARRAY);
						

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
	public void moveObj(GameObject obj, Vector3f pos)
	{
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;

		if(canPlaceHere(obj, x, y, z))
		{
			obj.position = pos;
		}
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
					return true;
					/*
					//Debug.println(this, "" + "Cur: " + cur.getName() + " " + cur.position + " Obj: " + obj.getName() + " " + obj.position);
					if(GeometryHelper.willIntersect(obj, cur, new Vector3f(x, y, z)))
					{
						//Debug.println(this, "They did it! They actually did it!!!");
						return false;
						//return true;
					}
					 */
				}
			}
		}

		return true;
	}
}

