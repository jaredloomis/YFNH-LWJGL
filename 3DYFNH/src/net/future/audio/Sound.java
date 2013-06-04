package net.future.audio;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

public class Sound 
{
	public Audio audio;
	public int pitch;
	public int gain;
	public boolean loop;
	public boolean isMusic;

	/**
	 * Defaults to a Sound Effect unless music is true
	 */
	public Sound(Audio a, boolean music, int pitch, int gain, boolean loop)
	{
		this.audio = a;
		this.pitch = pitch;
		this.gain = gain;
		this.loop = loop;
		this.isMusic = music;
	}

	public void play()
	{
		if(isMusic)
			this.audio.playAsMusic(pitch, gain, loop);
		else
			this.audio.playAsSoundEffect(pitch, gain, loop);

		SoundStore.get().poll(0);
	}
	
	public void toggle()
	{
		if(audio.isPlaying())
			audio.stop();
		else
			this.play();
	}
	
	public void stop()
	{
		audio.stop();
	}
}
