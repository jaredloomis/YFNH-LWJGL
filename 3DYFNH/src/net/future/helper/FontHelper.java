package net.future.helper;
import net.future.player.Player;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontHelper 
{	
	/**
     * Renders text using a font bitmap.
     *
     * @param string the string to render
     * @param textureObject the texture object containing the font glyphs
     * @param gridSize the dimensions of the bitmap grid (e.g. 16 -> 16x16 grid; 8 -> 8x8 grid)
     * @param x the x-coordinate of the bottom-left corner of where the string starts rendering
     * @param y the y-coordinate of the bottom-left corner of where the string starts rendering
     * @param characterWidth the width of the character
     * @param characterHeight the height of the character
     */
    public static void renderString(String string, int textureObject, int gridSize, float x, float y, float characterWidth, float characterHeight) {
        glPushAttrib(GL_TEXTURE_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT);
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureObject);
        // Enable linear texture filtering for smoothed results.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        // Enable additive blending. This means that the colours will be added to already existing colours in the
        // frame buffer. In practice, this makes the black parts of the texture become invisible.
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        // Store the current model-view matrix.
        glPushMatrix();
        // Offset all subsequent (at least up until 'glPopMatrix') vertex coordinates.
        glTranslatef(x, y, 0);
        glBegin(GL_QUADS);
        // Iterate over all the characters in the string.
        for (int i = 0; i < string.length(); i++) {
            // Get the ASCII-code of the character by type-casting to integer.
            int asciiCode = (int) string.charAt(i);
            // There are 16 cells in a texture, and a texture coordinate ranges from 0.0 to 1.0.
            final float cellSize = 1.0f / gridSize;
            // The cell's x-coordinate is the greatest integer smaller than remainder of the ASCII-code divided by the
            // amount of cells on the x-axis, times the cell size.
            float cellX = ((int) asciiCode % gridSize) * cellSize;
            // The cell's y-coordinate is the greatest integer smaller than the ASCII-code divided by the amount of
            // cells on the y-axis.
            float cellY = ((int) asciiCode / gridSize) * cellSize;
            glTexCoord2f(cellX, cellY + cellSize);
            glVertex2f(i * characterWidth / 3, y);
            glTexCoord2f(cellX + cellSize, cellY + cellSize);
            glVertex2f(i * characterWidth / 3 + characterWidth / 2, y);
            glTexCoord2f(cellX + cellSize, cellY);
            glVertex2f(i * characterWidth / 3 + characterWidth / 2, y + characterHeight);
            glTexCoord2f(cellX, cellY);
            glVertex2f(i * characterWidth / 3, y + characterHeight);
        }
        glEnd();
        glPopMatrix();
        glPopAttrib();
    }
    
	@SuppressWarnings("unchecked")
	public static UnicodeFont getWhiteArial() 
	{
		UnicodeFont font;
		java.awt.Font awtFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 30);
		font = new UnicodeFont(awtFont);
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return font;
	}
	
	@SuppressWarnings("unchecked")
	public static UnicodeFont getCustomFont(int size, String fontFace, java.awt.Color color, int effectID) 
	{
		int[] effects = {java.awt.Font.PLAIN, java.awt.Font.BOLD, java.awt.Font.ITALIC};
		UnicodeFont font;
		java.awt.Font awtFont = new java.awt.Font(fontFace, effects[effectID], size);
		font = new UnicodeFont(awtFont);
		font.getEffects().add(new ColorEffect(color));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return font;
	}

	public static void write(UnicodeFont font, String text, float x, float y)
	{
		font.drawString(x, y, text);
	}
	
	public static void drawCrosshair(UnicodeFont font)
	{
		font.drawString(Display.getWidth()/2, Display.getHeight()/2, "+");
	}
	
	public static void debugMenu(UnicodeFont font, Player p, float fps)
	{
		write(font, "Debug", 50, 10);
		write(font, "Player Position: " + p.position.toString(), 50, 50);
		write(font, "FPS: " + fps, 50, 100);
	}
	
	public static void pauseMenu(UnicodeFont font, Player p)
	{
		FontHelper.write(font, "Paused", (Display.getWidth()/2)-50, 100);
		FontHelper.write(font, "Return to Game - ESC", (Display.getWidth()/2)-150, 170);
		FontHelper.write(font, p.position.toString(), (Display.getWidth()/3), Display.getHeight()-100);
	}
}
