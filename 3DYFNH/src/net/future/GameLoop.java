package net.future;
import static org.lwjgl.opengl.GL11.*;
import net.future.audio.AudioManager;
import net.future.helper.FontHelper;
import net.future.player.Player;
import net.future.world.World;
import org.lwjgl.opengl.GL12;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.UnicodeFont;

public class GameLoop
{
	private World w;
	private Player p;
	private int updateRate = 80;
	private static int lastFPS=0;
	private static int fps = 0;
	public UnicodeFont font;
	private static float lastFrame = 0;
	private FloatBuffer perspectiveProjectionMatrix = BufferUtils.createFloatBuffer(16);
	private FloatBuffer orthographicProjectionMatrix = BufferUtils.createFloatBuffer(16);
	public static float delta = getDelta();

	/**
	 * Initial set-up, called once from Driver class
	 */
	public void initialize()
	{	
		//Init the time variables
		lastFrame=getTime();
		getTime();
		getDelta();
		
		this.setUpDisplay();
		this.setUpLighting();
		this.setUpFog();
		this.setUpWorld();

		//Allows 2d textures
		glEnable(GL_TEXTURE_2D);

		//Makes the default screen color black
		glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		glClearDepth(1.0f);

		//Make mouse invisible
		Mouse.setGrabbed(true);

		//Initialize and set up the font system
		this.font = FontHelper.getWhiteArial();
	}

	/**
	 * Initializes the main window
	 */
	private void setUpDisplay()
	{
		//Sets up a window
		try
		{
			Display.setDisplayMode(new DisplayMode(1280,720));
			Display.setVSyncEnabled(true);
			Display.setTitle("Your Future Needs Help PreAlpha v0.01");
			Display.setResizable(false);
			Display.create();
		}
		catch (LWJGLException ex)
		{
			Logger.getLogger(YFNH.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Enables the lighting system and sets up other
	 * display features of OpenGL
	 */
	private void setUpLighting() 
	{
		//Replaced by GL_RESCALE_NORMAL?
		//glEnable(GL_NORMALIZE);

		//Makes lighting smooth instead of flat
		glShadeModel(GL_SMOOTH);

		//Allow alpha in textures
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//Automatically normalize normals
		glEnable(GL12.GL_RESCALE_NORMAL);

		//Tell OpenGL to use the most correct, or highest quality, option when deciding
		//on Gl_PERSEPECTIVE_CORRECTION details. Other choices are GL_FASTEST and GL_DONT_CARE
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);

		//Enables 3D and depth. Also enables
		//Hardware acceleration
		glEnable(GL_DEPTH_TEST);

		//Store values in pixel data if depth
		//is less that or equal.
		glDepthFunc(GL_LEQUAL);

		//Enable lighting in general
		glEnable(GL_LIGHTING);

		//Do not render the backs of faces.
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		//Enable colors on faces / objects
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	private void setUpFog()
	{
		// Setup fog
		//glFogi(GL_FOG_MODE, GL_EXP);
		//glFogfv(GL_FOG_COLOR, fogColor);
		//glFogf(GL_FOG_DENSITY, 0.1f);
		//glHint(GL_FOG_HINT, GL_DONT_CARE);
		//glFogf(GL_FOG_START, 10);
		//glFogf(GL_FOG_END, 10 * 2);
		//glEnable(GL_FOG);
	}

	private void setUpWorld()
	{
		//Instantiate the world
		w = new World();

		//Set Up Player / Camera
		p = new Player(w, (float) Display.getWidth() / Display.getHeight(), 70);
		w.add(p);
		w.moveObj(p, new Vector3f(0, 0, 0));
		p.cam.applyPerspectiveMatrix();

		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		p.cam.applyPerspectiveMatrix();
	}

	/**
	 * Function called from driver class
	 */
	public void gameLoop()
	{
		//Main Update Loop continues until the window is closed.
		while(!Display.isCloseRequested())
		{
			this.render();
			this.update();

			//Sync the display to max FPS
			Display.sync(updateRate);
		}
		this.cleanUp();
	}

	/**
	 * Called every update
	 * Handles Rendering
	 */
	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//Texture arial = MyTextureLoader.getTexture("res/fonts/Arial.png");
		//arial.bind();
		//FontHelper.renderString("Test", arial.getTextureID(), 16, -0.9f, 0, 0.3f, 0.225f);
		//Somehow, all this code allows Text to go on the screen....
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(orthographicProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		glDisable(GL_LIGHTING);
		//// BEGIN DRAW TEXT ///

		//Draw Pause Menu Text if game is paused
		if(this.w.paused)
			FontHelper.pauseMenu(this.font, this.p);
		else
			//If game is not paused, draw a crosashair
			FontHelper.drawCrosshair(font);

		//Draw Debug screen if debug is on
		if(this.p.debugMenu)
			FontHelper.debugMenu(this.font, this.p, getFPS());
		
		//// END DRAW TEXT ///
		glEnable(GL_LIGHTING);
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		p.cam.applyPerspectiveMatrix();
	}

	/**
	 * Called every update
	 * handles everything that the
	 * render function does not handle.
	 */
	private void update()
	{	
		delta = getDelta();
		
		//Update the player
		p.playerUpdate();

		//Main update method for the world
		w.update();

		//Updates location of Orgin
		glLoadIdentity();
		
		//Moves the camera, Uses the player camera, ect.
		p.cameraUpdate();
		
		AudioManager.update();
	}
	
	private void cleanUp()
	{
		AudioManager.deleteAll();
		Display.destroy();
	}

	/**
	 * Returns the time the game has been running
	 * (In ticks)
	 */

	public static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static float getDelta()
	{
		long time = getTime();
		float delta = time - lastFrame;
		lastFrame = time;

		return delta;
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public static float getFPS() 
	{
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
		return fps;
	}
}
