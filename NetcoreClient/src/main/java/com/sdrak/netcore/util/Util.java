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
	
	public static final byte[] toDigitsArray(int value) 
	{
		final byte[] digits = new byte[4];
		digits[0] = (byte) (value >>> 24);
		digits[1] = (byte)(value >>> 16);
		digits[2] = (byte)(value >>> 8);
		digits[3] = (byte) value;
		return digits;
	}
	
	public static int digitsToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
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

