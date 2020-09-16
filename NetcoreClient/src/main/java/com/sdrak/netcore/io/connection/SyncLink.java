package com.sdrak.netcore.io.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public abstract class SyncLink<E extends NetClient<?>> extends NetConnection<E>
{
	protected final Queue<ByteBuffer> _readQueue = new ArrayDeque<>();
	
	public SyncLink(NetworkHandler<E> netHandler, final InetAddress inetAddress)
	{
		super(netHandler, inetAddress);
	}
	
	@Override
	protected void write(byte[] buffer, int begin, int end) throws IOException
	{
		_readQueue.offer(ByteBuffer.wrap(buffer));
	}

	public abstract void write() throws IOException;
	
//	protected abstract byte[] read(int begin, int end) throws IOException;
//	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
}
