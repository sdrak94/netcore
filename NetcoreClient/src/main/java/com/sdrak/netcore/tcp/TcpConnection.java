package com.sdrak.netcore.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncConnection;

public class TcpConnection<E extends NetClient<TcpConnection<E>>> extends SyncConnection<E>
{
	private final Socket _socket;
	
	public TcpConnection(NetworkHandler<E> netHandler, InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
		_socket = createSocket(socketAddress);
	}
	
	public TcpConnection(NetworkHandler<E> netHandler, Socket socket) throws IOException
	{
		super(netHandler, (InetSocketAddress) socket.getRemoteSocketAddress());
		_socket = socket;
	}
	
	public Socket getSocket()
	{
		return _socket;
	}

	@Override
	protected ByteBuffer read(int begin, int end) throws IOException 
	{
		byte[] buffer = new byte[end - begin];
		_socket.getInputStream().read(buffer, begin, end);
		final ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		return byteBuffer;
	}

	@Override
	protected void write(final ByteBuffer byteBuffer, int begin, int end) throws IOException
	{
		_socket.getOutputStream().write(byteBuffer.array(), begin, end);
	}
	
	private static Socket createSocket(final InetSocketAddress socketAddress)
	{
		try
		{
			return new Socket(socketAddress.getAddress(), socketAddress.getPort());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(socketAddress + " connecton failed!");
		}
	}
}
