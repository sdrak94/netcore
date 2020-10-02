package com.sdrak.netcore.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.sdrak.netcore.PacketDictionary;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncLink;
import com.sdrak.netcore.udp.readable.UGetSession;

public class UdpLink<E extends NetClient<?>> extends SyncLink<E>
{
	private final DatagramSocket _udpSocket;

	private long _sessionId;
	
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
		
		_netHandler.register(PacketDictionary.U__SET_SSESSION, UGetSession::new);
	}
	
	public void setSessionId(final long sessionId)
	{
		if (_sessionId == 0)
			_sessionId = sessionId;
	}
	
	public long getSessionId()
	{
		return _sessionId;
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
		for (final ByteBuffer buffer : _writeQueue)
		{
			
		}
	}
	
	@Override
	protected ByteBuffer writeHeaders(byte[] data)
	{
		final ByteBuffer headerBuffer = super.writeHeaders(data);
		headerBuffer.putLong(_sessionId);
		return headerBuffer;
	}
	
	@Override
	public int getHeaderSize()
	{
		return super.getHeaderSize() + 
				Long.BYTES;
	}
}
