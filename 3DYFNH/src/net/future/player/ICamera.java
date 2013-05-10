package net.future.player;

public interface ICamera {

    /** Processes mouse input and converts it in to camera movement. */
    public void processMouse();

    /**
     * Processes keyboard input and converts into camera movement.
     *
     * @param delta the elapsed time since the last frame update in milliseconds
     *
     * @throws IllegalArgumentException if delta is 0 or delta is smaller than 0
     */
    public void processKeyboard(float delta);

    /**
     * Move in the direction you're looking. That is, this method assumes a new coordinate system where the axis you're
     * looking down is the z-axis, the axis to your left is the x-axis, and the upward axis is the y-axis.
     *
     * @param dx the movement along the x-axis
     * @param dy the movement along the y-axis
     * @param dz the movement along the z-axis
     */
    public void moveFromLook(float dx, float dy, float dz);

    /**
     * Sets GL_PROJECTION to an orthographic projection matrix. The matrix mode will be returned it its previous value
     * after execution.
     */
    public void applyOrthographicMatrix();

    /** Enables or disables OpenGL states that will enhance the camera appearance. */
    public void applyOptimalStates();

    /**
     * Sets GL_PROJECTION to an perspective projection matrix. The matrix mode will be returned it its previous value
     * after execution.
     */
    public void applyPerspectiveMatrix();

    /** Applies the camera translations and rotations to GL_MODELVIEW. */
    public void applyTranslations();

}