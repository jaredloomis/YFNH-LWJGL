package net.future.helper;
import net.future.player.Player;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontHelper 
{	
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
