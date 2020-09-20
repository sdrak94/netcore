package com.sdrak.netcore.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncLink;

public class TcpLink<E extends NetClient<?>> extends SyncLink<E>
{
	private final SocketChannel _socketChannel;
	
	public TcpLink(NetworkHandler<E> netHandler, final SocketChannel socketChannel) throws IOException
	{
		super(netHandler, (InetSocketAddress) socketChannel.getRemoteAddress());
		_socketChannel = socketChannel;
		_socketChannel.configureBlocking(false);
	}
	
	public SocketChannel getSocket()
	{
		return _socketChannel;
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException 
	{
		final ByteBuffer buffer = ByteBuffer.allocate(end - begin);
		_socketChannel.read(buffer);
		return buffer.array();
	}

	@Override
	public void write() throws IOException
	{
		final ByteBuffer queueBuffer = _writeQueue.poll();
		if (queueBuffer != null)
		{
			_socketChannel.write(queueBuffer);
			write();
		}
	}
}
