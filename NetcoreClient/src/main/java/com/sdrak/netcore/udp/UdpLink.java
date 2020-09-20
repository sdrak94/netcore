package com.sdrak.netcore.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncLink;

public class UdpLink<E extends NetClient<?>> extends SyncLink<E>
{
	private final DatagramSocket _udpSocket;

	private final long _localSessionId;
	
//	private long _remoteSessionId;
	
	public UdpLink(final NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
		
		DatagramSocket udpSocket = null;
		try
		{

			udpSocket = new DatagramSocket();
			udpSocket.connect(socketAddress);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		_udpSocket = udpSocket;
		
		final UUID sessionUUID = UUID.randomUUID();
		_localSessionId = sessionUUID.getMostSignificantBits()  & Long.MAX_VALUE;
//		System.out.println(String.format("Created UUID: %08X", _sessionId));
//		_udpSock.bind(new InetSocketAddress(bindAddress, 1115));
	}
	
	public long getSessionId0()
	{
		return _localSessionId;
	}
	
	public long getSessionId1()
	{
		return _localSessionId;
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException 
	{
		byte[] buffer = new byte[end - begin];
		final DatagramPacket udpPacket = new DatagramPacket(buffer, begin, end);
		_udpSocket.receive(udpPacket);
		return buffer;
	}

	@Override
	protected void write(byte[] data, int begin, int end) throws IOException
	{
		final DatagramPacket udpPacket = new DatagramPacket(data, begin, end);
		_udpSocket.send(udpPacket);
	}

	@Override
	public void write() throws IOException
	{
		
	}
	
	@Override
	protected ByteBuffer writeHeaders(byte[] data)
	{
		final ByteBuffer headerBuffer = super.writeHeaders(data);
		headerBuffer.putLong(_localSessionId);
		return headerBuffer;
	}
	
	@Override
	public int getHeaderSize()
	{
		return super.getHeaderSize() + 
				Long.BYTES;
	}
}
