package net.future.helper;
import org.lwjgl.input.Keyboard;

public class Input
{
	public boolean[] keys;
	public boolean[] ident;


	public Input()
	{
		keys = new boolean[125];
	}

	public void update()
	{
		while(Keyboard.next())
		{
			if (Keyboard.getEventKeyState())
			{
				int cur = Keyboard.getEventKey();
				
				if(!keys[cur])
				{
					keys[cur]=true;
				}
				else if(keys[cur])
				{
					keys[cur]=false;
				}
			}
		}
	}

	public boolean getKeypress(int key)
	{
		this.update();

		if(keys[key])
		{
			keys[key]=false;
			return true;
		}
		return false;
	}
}
