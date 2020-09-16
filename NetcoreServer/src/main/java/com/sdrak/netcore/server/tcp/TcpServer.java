package com.sdrak.netcore.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.function.Function;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.NetServer;
import com.sdrak.netcore.tcp.TcpConnection;

public class TcpServer<E extends NetClient<TcpConnection<E>>> extends NetServer<E, TcpConnection<E>>
{
	private final ServerSocket _serverSocket;
	
	public TcpServer(Collection<E> aliveClients, Function<TcpConnection<E>, E> clientFactory, int port) throws IOException
	{
		super(aliveClients, clientFactory);
		_serverSocket = new ServerSocket(port);
	}
	
	@Override
	protected final E accept()
	{
		try
		{	
			final Socket sock = _serverSocket.accept();
			final TcpConnection<E> tcpCon = new TcpConnection<>(_netHandler, sock);
			final E tcpClient = forgeClient(tcpCon);
			return tcpClient;
		}
		catch (Exception e)
		{	e.printStackTrace();
			return null;
		}
	}
}
