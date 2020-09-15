package com.sdrak.netcore.io.connection;

import java.io.IOException;
import java.net.InetAddress;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public abstract class AsyncConnection<E extends NetClient<?>> extends NetConnection<E>
{
	public AsyncConnection(NetworkHandler<E> netHandler, final InetAddress inetAddress)
	{
		super(netHandler, inetAddress);
	}

	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
}
