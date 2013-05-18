package net.future;

public class YFNH
{	
	public static void main(String[] args)
	{
		GameLoop loop = new GameLoop();
		
		loop.initialize();
		loop.gameLoop();
	}
}