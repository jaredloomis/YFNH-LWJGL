package net.future.player;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static org.lwjgl.opengl.ARBDepthClamp.GL_DEPTH_CLAMP;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.util.glu.Project.gluPerspective;
import net.future.gameobject.GameObject;
import net.future.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Camera implements ICamera
{
	public float mouseSpeed, maxLookUp, maxLookDown, aspectRatio,
				 moveSpeed, fov, zNear, zFar;
	
	public World world;
	
	public GameObject parent;
	
	public Camera(World w, GameObject parent, float aspect, float fov)
	{
		this.world = w;
		this.parent = parent;
		this.mouseSpeed = 1f;
		this.maxLookUp = 90;
		this.maxLookDown = -90;
		this.zNear = 0.01f;
		this.zFar = 100;
		this.aspectRatio=aspect;
		this.fov = fov;
		this.moveSpeed = 1;
	}
	
	@Override
	public void processMouse() 
	{
		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
		
		if (this.parent.rotation.y + mouseDX >= 360) 
		{
			this.parent.rotation.y = this.parent.rotation.y + mouseDX - 360;
		} 
		else if (this.parent.rotation.y + mouseDX < 0) 
		{
			this.parent.rotation.y = 360 - this.parent.rotation.y + mouseDX;
		} 
		else 
		{
			this.parent.rotation.y += mouseDX;
		}
		
		
		if (this.parent.rotation.x - mouseDY >= maxLookDown && this.parent.rotation.x - mouseDY <= maxLookUp) 
		{
			this.parent.rotation.x += -mouseDY;
		} 
		else if (this.parent.rotation.x - mouseDY < maxLookDown) 
		{
			this.parent.rotation.x = maxLookDown;
		} 
		else if (this.parent.rotation.x - mouseDY > maxLookUp) 
		{
			this.parent.rotation.x = maxLookUp;
		}
	}

	@Override
	public void processKeyboard(float delta) 
	{
		if (delta <= 0) {
			throw new IllegalArgumentException("delta " + delta + " is 0 or is smaller than 0");
		}

		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

		if (keyUp && keyRight && !keyLeft && !keyDown) {
			moveFromLook(delta * 0.003f * moveSpeed, 0, -delta * 0.003f * moveSpeed);
		}
		if (keyUp && keyLeft && !keyRight && !keyDown) {
			moveFromLook(-delta * 0.003f * moveSpeed, 0, -delta * 0.003f * moveSpeed);
		}
		if (keyUp && !keyLeft && !keyRight && !keyDown) {
			moveFromLook(0, 0, -delta * 0.003f * moveSpeed);
		}
		if (keyDown && keyLeft && !keyRight && !keyUp) {
			moveFromLook(-delta * 0.003f * moveSpeed, 0, delta * 0.003f * moveSpeed);
		}
		if (keyDown && keyRight && !keyLeft && !keyUp) {
			moveFromLook(delta * 0.003f * moveSpeed, 0, delta * 0.003f * moveSpeed);
		}
		if (keyDown && !keyUp && !keyLeft && !keyRight) {
			moveFromLook(0, 0, delta * 0.003f * moveSpeed);
		}
		if (keyLeft && !keyRight && !keyUp && !keyDown) {
			moveFromLook(-delta * 0.003f * moveSpeed, 0, 0);
		}
		if (keyRight && !keyLeft && !keyUp && !keyDown) {
			moveFromLook(delta * 0.003f * moveSpeed, 0, 0);
		}
		if (flyUp && !flyDown) {
			this.world.moveObj(this.parent, new Vector3f(this.parent.position.x, this.parent.position.y + delta * 0.003f * moveSpeed, this.parent.position.z));
		}
		if (flyDown && !flyUp) {
			this.world.moveObj(this.parent, new Vector3f(this.parent.position.x, this.parent.position.y - delta * 0.003f * moveSpeed, this.parent.position.z));
		}
	}

	@Override
	public void moveFromLook(float dx, float dy, float dz)
	{
		float movX = (float)(dx * sin(toRadians(this.parent.rotation.y - 90)) + dz * sin(toRadians(this.parent.rotation.y)));
		float movY = (float)(dy * sin(toRadians(this.parent.rotation.x - 90)) + dz * sin(toRadians(this.parent.rotation.x)));
		float movZ = (float)(dx * cos(toRadians(this.parent.rotation.y - 90)) + dz * cos(toRadians(this.parent.rotation.y)));
		
		float newZ = this.parent.position.z + (movZ);
		float newX = this.parent.position.x - (movX);
		float newY = this.parent.position.y + movY;
		
		this.world.moveObj(this.parent, new Vector3f(newX, newY, newZ));
	}

	/**
	 * Sets GL_PROJECTION to an orthographic projection matrix. The matrix mode will be returned it its previous value
	 * after execution.
	 */
	@Override
	public void applyOrthographicMatrix() 
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-aspectRatio, aspectRatio, -1, 1, 0, zFar);
		
		gluPerspective(fov, -aspectRatio, 0f, zFar);
		glPopAttrib();
	}

	/**
	 * Enables or disables OpenGL states that will enhance the camera appearance. Enable GL_DEPTH_CLAMP if
	 * ARB_depth_clamp is supported
	 */
	@Override
	public void applyOptimalStates() 
	{
		if (GLContext.getCapabilities().GL_ARB_depth_clamp) 
		{
			glEnable(GL_DEPTH_CLAMP);
		}
	}

	/**
	 * Sets GL_PROJECTION to an perspective projection matrix. The matrix mode will be returned it its previous value
	 * after execution.
	 */
	@Override
	public void applyPerspectiveMatrix() 
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, aspectRatio, zNear, zFar);
		glPopAttrib();
	}

	/** 
	 * Applies the camera translations and rotations to GL_MODELVIEW. 
	 */
	@Override
	public void applyTranslations() 
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_MODELVIEW);
		glRotatef(this.parent.rotation.x, 1, 0, 0);
		glRotatef(this.parent.rotation.y, 0, 1, 0);
		glRotatef(this.parent.rotation.z, 0, 0, 1);
		glTranslatef(-this.parent.position.x, -this.parent.position.y, -this.parent.position.z);
		glPopAttrib();
	}
}
