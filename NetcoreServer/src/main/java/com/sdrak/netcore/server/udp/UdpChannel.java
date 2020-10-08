package com.sdrak.netcore.server.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import com.sdrak.netcore.NetDictionary;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.NetServer;
import com.sdrak.netcore.server.udp.io.recv.URecvReqSession;
import com.sdrak.netcore.udp.UdpLink;

import gnu.trove.map.hash.TLongObjectHashMap;

public class UdpChannel<E extends NetClient<UdpLink<E>>> extends NetServer<E, UdpLink<E>>
{
	private final TLongObjectHashMap<UdpLink<E>> _sessionsMap = new TLongObjectHashMap<>();
	
	private final Deque<E> _acceptQueue = new ArrayDeque<>();
	
	private final Selector _ioSelector;
	private final DatagramChannel _serverSocket;
	
	public UdpChannel(Collection<E> aliveClients, final Function<UdpLink<E>, E> clientFactory, int port) throws IOException
	{
		super(aliveClients, clientFactory);
		_ioSelector = Selector.open();
		
		_serverSocket = DatagramChannel.open();
		_serverSocket.configureBlocking(false);
		_serverSocket.bind(new InetSocketAddress(port));
		_serverSocket.register(_ioSelector, SelectionKey.OP_READ);
		
		register(NetDictionary.U_RECV__REQ_SESSION, URecvReqSession::new);
	}
	
	@Override
	protected final E accept()
	{
		final E client = _acceptQueue.poll();
		
		if (client != null)
			return client;
		
		sleep(10);
		fillQueue();
		
		return accept();
	}
	
	private void fillQueue()
	{
		try
		{	
			_ioSelector.select();
			final Set<SelectionKey> selectedKeys = _ioSelector.selectedKeys();
			
			final Iterator<SelectionKey> iter = selectedKeys.iterator();
			
			while (iter.hasNext())
			{
				final SelectionKey selectedKey = iter.next();
				
				final DatagramChannel datagramChannel = (DatagramChannel) selectedKey.channel();
				@SuppressWarnings("unchecked")
				UdpLink<E> udpLink = (UdpLink<E>) selectedKey.attachment();
				
				if (selectedKey.isReadable())
				{

					
					ByteBuffer readBuffer = ByteBuffer.allocate(12);
					final byte[] b = readBuffer.array();
					
					final InetSocketAddress sockAddr = (InetSocketAddress) datagramChannel.receive(readBuffer);
					readBuffer.flip();
					if (sockAddr == null)
						continue;
					
					final int dataLen = readBuffer.getInt();
					final long sessionId = readBuffer.getLong();
					
					if (udpLink == null)
						udpLink = fetchLink(sessionId, sockAddr);
					
					selectedKey.interestOps(SelectionKey.OP_WRITE);
					
					System.out.println(String.format("Server: Recieved %d bytes from session %08X", dataLen, sessionId));
					
					udpLink.read();
				}
				else if (selectedKey.isWritable())
				{
					if (udpLink == null)
						return; //?
					selectedKey.interestOps(SelectionKey.OP_READ);
				}
			}
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
	
	public UdpLink<E> fetchLink(final long sessionId, final InetSocketAddress sockAddr) throws IOException
	{
		UdpLink<E> udpLink = _sessionsMap.get(sessionId);
		if (udpLink == null)
		{
			udpLink = new UdpLink<>(_netHandler, sockAddr);
			_sessionsMap.put(sessionId, udpLink);
			final E client = forgeClient(udpLink);
			_acceptQueue.add(client);
			return udpLink;
		}
		
		return udpLink;
		
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
