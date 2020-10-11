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
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.NetServer;
import com.sdrak.netcore.tcp.TcpLink;

public class TcpChannel<E extends NetClient<TcpLink<E>>> extends NetServer<E, TcpLink<E>>
{
	private final Deque<E> _acceptQueue = new ArrayDeque<>();
	
	private final Selector _acceptSelector;
	private final Selector _ioSelector;
	private final ServerSocketChannel _serverSocket;
	
	public TcpChannel(Collection<E> aliveClients, final Function<TcpLink<E>, E> clientFactory, int port) throws IOException
	{
		super(aliveClients, clientFactory);
		_acceptSelector = Selector.open();
		_ioSelector = Selector.open();
		
		_serverSocket = ServerSocketChannel.open();

		_serverSocket.configureBlocking(false);
		
		_serverSocket.bind(new InetSocketAddress(port));
		_serverSocket.register(_acceptSelector, SelectionKey.OP_ACCEPT);
		
		final Thread ioThread = new Thread(this::startIO);
		ioThread.start();
	}
	
	@Override
	protected final E accept()
	{
		try
		{
			_acceptSelector.select();
			final SocketChannel socketChannel = _serverSocket.accept();
			final TcpLink<E> tcpLink = new TcpLink<>(_netHandler, socketChannel);
			final E client = forgeClient(tcpLink);
			
			socketChannel.register(_ioSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, tcpLink);
			
			_acceptQueue.offer(client);
			return client;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void startIO()
	{
		while (true)
			performIO();
	}
	
	private void performIO()
	{
		try
		{
			_ioSelector.selectNow();
			final Set<SelectionKey> selectedKeys = _ioSelector.selectedKeys();
			
			Iterator<SelectionKey> iter = selectedKeys.iterator();
			
			while (iter.hasNext())
			{
				final SelectionKey selectedKey = iter.next();
				@SuppressWarnings("unchecked")
				final TcpLink<E> tcpLink = (TcpLink<E>) selectedKey.attachment();

				if (tcpLink == null) // no client attached ?
					continue;
				
				if (selectedKey.isReadable())
					read(selectedKey, tcpLink);
				if (selectedKey.isWritable())
					write(selectedKey, tcpLink);
				
				iter.remove();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void read(final SelectionKey selectionKey, final TcpLink<E> tcpLink) throws IOException
	{
		tcpLink.read();
		selectionKey.interestOps(SelectionKey.OP_WRITE);
	}
	
	private void write(final SelectionKey selectionKey, final TcpLink<E> tcpLink) throws IOException
	{
		tcpLink.write();
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
//	@Override
//	protected final E accept()
//	{
//		final E client = _acceptQueue.poll();
//		
//		if (client != null)
//			return client;
//		
//		sleep(500);
//		fillQueue();
//		
//		return accept();
//
//	}
//	
//	private void fillQueue()
//	{
//		try
//		{	
//			_acceptSelector.select();
//			final Set<SelectionKey> selectedKeys = _acceptSelector.selectedKeys();
//			
//			Iterator<SelectionKey> iter = selectedKeys.iterator();
//			
//			while (iter.hasNext())
//			{
//				final SelectionKey selectedKey = iter.next();
//				if (selectedKey.isAcceptable())
//				{
//					final SocketChannel socketChannel = _serverSocket.accept();
//					final TcpLink<E> tcpCon = new TcpLink<>(_netHandler, socketChannel);
//					final E client = forgeClient(tcpCon);
//					_acceptQueue.offer(client);
//				}
//			}
//		}
//		catch (Exception e)
//		{	e.printStackTrace();
//		}
//	}
}
