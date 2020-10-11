package com.sdrak.netcore.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncConnection;

public class TcpConnection<E extends NetClient<TcpConnection<E>>> extends SyncConnection<E>
{
	private final SocketChannel _socket;
	
	public TcpConnection(NetworkHandler<E> netHandler, InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
		_socket = createSocket(socketAddress);
	}
	
	public TcpConnection(NetworkHandler<E> netHandler, SocketChannel socket) throws IOException
	{
		super(netHandler, (InetSocketAddress) socket.getRemoteAddress());
		_socket = socket;
	}
	
	public SocketChannel getSocket()
	{
		return _socket;
	}

	@Override
	protected ByteBuffer read(int begin, int end) throws IOException 
	{
		final int packetSize = end - begin;
		final ByteBuffer packetBuffer = ByteBuffer.allocate(packetSize);
		_socket.read(packetBuffer);
		return packetBuffer;
	}

	@Override
	protected void write(final ByteBuffer byteBuffer, int begin, int end) throws IOException
	{
		_socket.write(byteBuffer);
	}
	
	private static SocketChannel createSocket(final InetSocketAddress socketAddress)
	{
		try
		{
			final SocketChannel socketChannel = SocketChannel.open();
			socketChannel.connect(socketAddress);
			return socketChannel;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(socketAddress + " connecton failed!");
		}
	}
}
