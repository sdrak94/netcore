package com.sdrak.netcore.tcp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncLink;

public class TcpLink<E extends NetClient<?>> extends SyncLink<E>
{
	private final SocketChannel _sock;
	
	public TcpLink(NetworkHandler<E> netHandler, SocketChannel sock) throws IOException
	{
		super(netHandler, sock.socket().getInetAddress());
		_sock = sock;
		_sock.configureBlocking(false);
	}
	
	public SocketChannel getSocket()
	{
		return _sock;
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException 
	{
//		byte[] buffer = new byte[end - begin];
		final ByteBuffer buffer = ByteBuffer.allocate(end - begin);
		_sock.read(buffer);
		return buffer.array();
	}

	@Override
	public void write() throws IOException
	{
		final ByteBuffer queueBuffer = _readQueue.poll();
		if (queueBuffer != null)
		{
			_sock.write(queueBuffer);
			write();
		}
	}
}
