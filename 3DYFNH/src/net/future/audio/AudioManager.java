package net.future.audio;
import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.io.File;
import java.io.IOException;

public class AudioManager 
{
	public static Sound infiniteRegression = new Sound(AudioManager.loadAudioFile("res"+File.separatorChar+"audio"+File.separatorChar+"AAL_AnInfinteRegression.wav"), true, 1, 1, false);
	
	public static void update()
	{
	}
	
	/**
	 * Returns An Audio object of the specified WAV file
	 */
	public static Audio loadAudioFile(String loc)
	{
		Audio data = null;

		try {
			data = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(loc));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	/**
	 * Cleanup function. should be called when application closes.
	 */
	public static void deleteAll()
	{
		AL.destroy();
	}
}
