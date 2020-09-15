package com.sdrak.netcore;

public class Console
{
	public static void write(Object obj)
	{
		System.out.println(obj);
	}
	
	public static void write(Object ...obj)
	{
		for (Object ob : obj)
			System.out.println(ob);
	}
	
	public static void writef(String msg, Object ... args)
	{
		System.out.printf(msg, args);
	}
}
