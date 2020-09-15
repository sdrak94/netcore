package com.sdrak.netcore.tcp;

import java.io.IOException;
import java.net.Socket;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncConnection;

public class TcpConnection<E extends NetClient<TcpConnection<E>>> extends SyncConnection<E>
{
	private final Socket _sock;
	
	public TcpConnection(NetworkHandler<E> netHandler, Socket sock)
	{
		super(netHandler, sock.getInetAddress());
		_sock = sock;
	}
	
	public Socket getSocket()
	{
		return _sock;
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException 
	{
		byte[] buffer = new byte[end - begin];
		_sock.getInputStream().read(buffer, begin, end);
		return buffer;
	}

	@Override
	protected void write(byte[] data, int begin, int end) throws IOException
	{
		_sock.getOutputStream().write(data, begin, end);
	}
}