package com.sdrak.netcore.io.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public abstract class SyncLink<E extends NetClient<?>> extends NetConnection<E>
{
	protected final Queue<ByteBuffer> _writeQueue = new ConcurrentLinkedQueue<>();
	
	public SyncLink(NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
	}
	
	@Override
	protected void write(final ByteBuffer byteBuffer, int begin, int end) throws IOException
	{
		_writeQueue.offer(byteBuffer);
	}

	public abstract void write() throws IOException;
	
//	protected abstract byte[] read(int begin, int end) throws IOException;
//	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
}
