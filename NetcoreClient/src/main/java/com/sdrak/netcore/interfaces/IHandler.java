package com.sdrak.netcore.interfaces;

import java.util.function.Supplier;

import com.sdrak.netcore.io.ReadablePacket;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.io.client.NetClient;

public interface IHandler<E extends NetClient<?>>
{
	public default void register(int opcode, Supplier<? extends ReadablePacket<E>> rpacketClass)
	{
		register(ClientState.ONLINE, opcode, rpacketClass);
	}
	
	public void register(final ClientState state, int opcode, Supplier<? extends ReadablePacket<E>> rpacketClass);
}
