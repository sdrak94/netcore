package com.sdrak.netcore.server.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.NetServer;
import com.sdrak.netcore.tcp.TcpLink;

public class TcpChannel<E extends NetClient<TcpLink<E>>> extends NetServer<E>
{
	private final Deque<E> _acceptQueue = new ArrayDeque<>();
	
	private final Selector _selector;
	private final ServerSocketChannel _serverSocket;
	
	public TcpChannel(Collection<E> aliveClients, Class<E> clientClass, int port) throws IOException
	{
		super(aliveClients, clientClass);
		_selector = Selector.open();
		_serverSocket = ServerSocketChannel.open();
		
		_serverSocket.bind(new InetSocketAddress(port));
		_serverSocket.configureBlocking(false);
		_serverSocket.register(_selector, SelectionKey.OP_ACCEPT);
	}
	
	@Override
	protected final E accept()
	{
		if (_acceptQueue.isEmpty())
			fillQueue();
		
		return _acceptQueue.poll();

	}
	
	private void fillQueue()
	{
		try
		{	
			_selector.select();
			final Set<SelectionKey> selectedKeys = _selector.selectedKeys();
			
			for (final SelectionKey selectedKey : selectedKeys)
			{
				if (selectedKey.isAcceptable())
				{
					final SocketChannel socketChannel = _serverSocket.accept();
					final TcpLink<E> tcpCon = new TcpLink<>(_netHandler, socketChannel);
					final E client = forgeClient(tcpCon);
					_acceptQueue.offer(client);
				}
			}
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
}
