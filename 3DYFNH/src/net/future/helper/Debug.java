package net.future.helper;

public class Debug 
{
	public static void println(Object callingClass, Object txt)
	{
		System.out.println("["+callingClass.getClass().getSimpleName() + ".class]: " + txt);
	}
}
