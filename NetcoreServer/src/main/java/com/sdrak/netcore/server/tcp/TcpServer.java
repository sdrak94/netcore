package com.sdrak.netcore.server.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.function.Function;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.NetServer;
import com.sdrak.netcore.tcp.TcpConnection;

public class TcpServer<E extends NetClient<TcpConnection<E>>> extends NetServer<E, TcpConnection<E>>
{
	private final ServerSocketChannel _serverSocket;
	
	public TcpServer(Collection<E> aliveClients, Function<TcpConnection<E>, E> clientFactory, int port) throws IOException
	{
		super(aliveClients, clientFactory);
		_serverSocket = ServerSocketChannel.open();
		_serverSocket.bind(new InetSocketAddress(port));
	}
	
	@Override
	protected final E accept()
	{
		try
		{	
			final SocketChannel sock = _serverSocket.accept();
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
