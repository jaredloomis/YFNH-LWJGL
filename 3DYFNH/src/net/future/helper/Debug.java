package net.future.helper;

public class Debug 
{
	public static void println(Object txt)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		StackTraceElement one = stackTraceElements[2];
		String className = one.getClassName();
		System.out.println("["+className.substring(className.lastIndexOf('.')+1, className.length()) + ".class" + " line " + one.getLineNumber() + "]: " + txt);
	}
}
