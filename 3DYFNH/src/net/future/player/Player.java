package net.future.player;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import net.future.gameobject.GameObject;
import net.future.helper.Input;
import net.future.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Player extends GameObject
{
	//public Light light;
	private Input in;
	public boolean debugMenu;
	public Camera cam;
	
	//Controls
	public int debugButton = Keyboard.KEY_I;
	public int close = Keyboard.KEY_X;
	public int pause = Keyboard.KEY_ESCAPE;

	public Player(World w, float aspect, float fov)
	{
		super(w);
		this.in = new Input();
		this.cam = new Camera(w, this, aspect, fov);
		this.debugMenu = false;
	}

	public void playerUpdate()
	{
		in.update();
		boolean pausePressed = in.getKeypress(pause);

		if(pausePressed)
		{
			if(Mouse.isGrabbed())
			{
				this.debugMenu = false;
				this.world.paused = true;
				Mouse.setGrabbed(false);
			}
			else
			{
				this.world.paused = false;
				Mouse.setGrabbed(true);
			}
		}

		//If game is paused, we can't move or perform actions
		if(!this.world.paused)
		{
			cam.processMouse();
			cam.processKeyboard(16);
			
			this.handleInput();
			
			//this.moveLight();
			//this.light.update();
		}
	}
	
	private void handleInput()
	{
		//Toggle debug screen
		if(in.getKeypress(debugButton))
		{
			if(debugMenu)
				debugMenu=false;
			else
				debugMenu=true;
		}
		//Close screen
		if(in.getKeypress(close))
		{
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) 
		{
            this.world.lights[0].lightPosition.flip();
            this.world.lights[0].lightPosition.clear();
            this.world.lights[0].lightPosition.put(new float[]{this.position.x, this.position.y, this.position.z, 1});
            this.world.lights[0].lightPosition.flip();
        }
	}

	public void cameraUpdate()
	{
		//Use the player's camera
		this.cam.applyOptimalStates();
		this.cam.applyTranslations();
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		glPopMatrix();
		this.cam.applyPerspectiveMatrix();
	}

	@Override
	public String getName()
	{
		return "Player";
	}

}