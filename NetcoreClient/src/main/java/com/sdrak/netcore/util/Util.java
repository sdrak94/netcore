package com.sdrak.netcore.util;

import java.util.Arrays;

public class Util
{
	public static <T> T[] concat(T[] first, T[] second)
	{
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static byte[] concat(byte[] first, byte[] second)
	{
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static final void writeLong(final byte[] buffer, long value, final int offset)
	{
		buffer[0 + offset] = (byte) (value >>> 24);
		buffer[1 + offset] = (byte) (value >>> 16);
		buffer[2 + offset] = (byte) (value >>> 8);
		buffer[3 + offset] = (byte)  value;
	}

	public static final void writeInt(final byte[] buffer, int value, final int offset)
	{
		buffer[0 + offset] = (byte) (value >>> 24);
		buffer[1 + offset] = (byte) (value >>> 16);
		buffer[2 + offset] = (byte) (value >>> 8);
		buffer[3 + offset] = (byte)  value;
	}
	
	public static final void writeInt(final byte[] buffer, int value)
	{
		writeInt(buffer, value, 0);
	}

	public static int readInt(byte[] buffer) 
	{
	    return readInt(buffer, 0);
	}
	
	public static int readInt(byte[] buffer, int offset) 
	{
	    return   buffer[offset + 3] & 0xFF        |
	            (buffer[offset + 2] & 0xFF) << 8  |
	            (buffer[offset + 1] & 0xFF) << 16 |
	            (buffer[offset    ] & 0xFF) << 24;
	}
	
	public static int intRead(byte[] buffer, int offset) 
	{
	    return  (buffer[offset + 3] & 0xFF) << 24 |
	            (buffer[offset + 2] & 0xFF) << 16 |
	            (buffer[offset + 1] & 0xFF) << 8  |
	    		 buffer[offset + 0] & 0xFF;
	}

//	public static int readInt(byte[] b, int offset)
//	{
//		return b[offset + 3] & 0xFF | (b[offset + 2] & 0xFF) << 8 | (b[offset + 1] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
//	}
//
//	public static int readInt(byte[] b)
//	{
//		return readInt(b, 0);
//	}
	
//	public static int intRead(byte[] b, int offset)
//	{
//		return readInt(new byte[] {b[3], b[2], b[1], b[0]}, offset);
//	}

	public static int intRead(byte[] b)
	{
		return intRead(b, 0);
	}

	public static void printAsHex(byte[] data)
	{
		for (byte b : data)
			System.out.print("0x" + String.format("%02x", b) + " ");
		System.out.println();
	}

	public static String hexValue(Number n)
	{
		return "0x" + String.format("%02x", n);
	}

	public static void main(String[] args)
	{
		final String test = " t  test   .  ";
		System.out.println(test.trim());
	}
}
