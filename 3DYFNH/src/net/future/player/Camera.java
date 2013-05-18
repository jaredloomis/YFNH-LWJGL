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
	public int kUp, kDown, kLeft, kRight, kJump, kfDown;

	public float mouseSpeed, maxLookUp, maxLookDown, aspectRatio,
	moveSpeed, fov, zNear, zFar, jumpForce;

	public World world;

	public GameObject parent;

	public boolean fly;

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
		this.moveSpeed = 1f;
		this.jumpForce = 0.18f;
		this.fly = true;

		this.kUp = Keyboard.KEY_W;
		this.kDown = Keyboard.KEY_S;
		this.kLeft = Keyboard.KEY_A;
		this.kRight = Keyboard.KEY_D;
		this.kJump = Keyboard.KEY_SPACE;
		this.kfDown = Keyboard.KEY_LSHIFT;
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
		float d=delta;
		if (d <= 0)
		{
			d = 16;
			//throw new IllegalArgumentException("delta (" + delta + ") is 0 or is smaller than 0");
		}

		boolean keyUp = Keyboard.isKeyDown(this.kUp);
		boolean keyDown = Keyboard.isKeyDown(this.kDown);
		boolean keyLeft = Keyboard.isKeyDown(this.kLeft);
		boolean keyRight = Keyboard.isKeyDown(this.kRight);
		boolean flyUp = Keyboard.isKeyDown(this.kJump);
		boolean flyDown = Keyboard.isKeyDown(this.kfDown);

		if (keyUp && keyRight && !keyLeft && !keyDown) {
			moveFromLook(d * 0.003f * moveSpeed, 0, -d * 0.003f * moveSpeed);
		}
		if (keyUp && keyLeft && !keyRight && !keyDown) {
			moveFromLook(-d * 0.003f * moveSpeed, 0, -d * 0.003f * moveSpeed);
		}
		if (keyUp && !keyLeft && !keyRight && !keyDown) {
			moveFromLook(0, 0, -d * 0.003f * moveSpeed);
		}
		if (keyDown && keyLeft && !keyRight && !keyUp) {
			moveFromLook(-d * 0.003f * moveSpeed, 0, d * 0.003f * moveSpeed);
		}
		if (keyDown && keyRight && !keyLeft && !keyUp) {
			moveFromLook(d * 0.003f * moveSpeed, 0, d * 0.003f * moveSpeed);
		}
		if (keyDown && !keyUp && !keyLeft && !keyRight) {
			moveFromLook(0, 0, d * 0.003f * moveSpeed);
		}
		if (keyLeft && !keyRight && !keyUp && !keyDown) {
			moveFromLook(-d * 0.003f * moveSpeed, 0, 0);
		}
		if (keyRight && !keyLeft && !keyUp && !keyDown) {
			moveFromLook(d * 0.003f * moveSpeed, 0, 0);
		}

		if(fly)
		{
			if (flyUp && !flyDown) {
				this.world.moveObj(this.parent, new Vector3f(this.parent.position.x, this.parent.position.y + d * 0.003f * moveSpeed, this.parent.position.z));
			}
			if (flyDown && !flyUp) {
				this.world.moveObj(this.parent, new Vector3f(this.parent.position.x, this.parent.position.y - d * 0.003f * moveSpeed, this.parent.position.z));
			}
		}
		else
		{
			if (flyUp && this.parent.grounded)
			{
				this.parent.velocity.y=(this.jumpForce) + this.parent.velocity.y;
			}
		}
	}

	@Override
	public void moveFromLook(float dx, float dy, float dz)
	{
		float movY = 0;

		float movX = (float)(dx * sin(toRadians(this.parent.rotation.y - 90)) + dz * sin(toRadians(this.parent.rotation.y)));
		if(fly)
			movY = (float)(dy * sin(toRadians(this.parent.rotation.x - 90)) + dz * sin(toRadians(this.parent.rotation.x)));
		float movZ = (float)(dx * cos(toRadians(this.parent.rotation.y - 90)) + dz * cos(toRadians(this.parent.rotation.y)));

		this.parent.velocity = Vector3f.add(new Vector3f(-movX, movY, movZ), this.parent.velocity, null);
	}

	/**
	 * Sets GL_PROJECTION to an orthographic projection matrix. The matrix mode will be returned to its previous value
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
