package com.sdrak.netcore.factory;

import java.util.UUID;

public class IdFactory
{
	public long generateSessionId()
	{		
		final UUID sessionUUID = UUID.randomUUID();
		return sessionUUID.getMostSignificantBits()  & Long.MAX_VALUE;
	}
	
	private static final class InstanceHolder
	{
		private static final IdFactory _idFactory = new IdFactory();
	}
	
	public static final IdFactory getInstance()
	{
		return InstanceHolder._idFactory;
	}
}