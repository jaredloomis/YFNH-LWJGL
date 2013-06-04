package net.future.player;
import net.future.GameLoop;
import net.future.audio.AudioManager;
import net.future.gameobject.GameObject;
import net.future.helper.Input;
import net.future.physpacks.PhysPlayer;
import net.future.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Player extends GameObject
{
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
		this.grounded = false;
		this.in = new Input();
		this.cam = new Camera(w, this, aspect, fov);
		this.debugMenu = false;
		this.cam.applyPerspectiveMatrix();
		this.physics = new PhysPlayer();
	}

	public void playerUpdate()
	{
		//Updates which keys are down, pressed, ect
		in.update();
		
		boolean pausePressed = in.getKeypress(pause);

		//Player must always be able to pause
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
			cam.processKeyboard(GameLoop.delta);
			
			this.handleInput();
		}
	}
	
	private void handleInput()
	{
		//Toggle debug screen
		if(in.getKeypress(debugButton))
		{
			AudioManager.infiniteRegression.toggle();
			
			if(debugMenu)
				debugMenu=false;
			else
				debugMenu=true;
		}
		//Close screen
		if(in.getKeypress(close))
		{
			
		}
		
		//Set position of light 0 of the world to player position
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
		this.cam.applyTranslations();
	}

	@Override
	public String getName()
	{
		return "Player";
	}

}