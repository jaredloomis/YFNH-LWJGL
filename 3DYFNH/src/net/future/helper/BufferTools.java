package net.future.helper;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class BufferTools 
{
	public static FloatBuffer asFlippedFloatBuffer(Matrix4f matrix4f) 
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix4f.store(buffer);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer asFlippedFloatBuffer(float... values) 
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
}
