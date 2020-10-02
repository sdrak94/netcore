package com.sdrak.netcore.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncConnection;

public class TcpConnection<E extends NetClient<TcpConnection<E>>> extends SyncConnection<E>
{
	private final Socket _socket;
	
	public TcpConnection(NetworkHandler<E> netHandler, InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
		
		Socket socket = null;
		try
		{
			socket = new Socket(socketAddress.getAddress(), socketAddress.getPort());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		_socket = socket;
		connect();
	}
	
	public TcpConnection(NetworkHandler<E> netHandler, Socket socket) throws IOException
	{
		super(netHandler, (InetSocketAddress) socket.getRemoteSocketAddress());
		_socket = socket;
		connect();
	}
	
	public Socket getSocket()
	{
		return _socket;
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException 
	{
		byte[] buffer = new byte[end - begin];
		_socket.getInputStream().read(buffer, begin, end);
		return buffer;
	}

	@Override
	protected void write(byte[] data, int begin, int end) throws IOException
	{
		_socket.getOutputStream().write(data, begin, end);
	}
}
