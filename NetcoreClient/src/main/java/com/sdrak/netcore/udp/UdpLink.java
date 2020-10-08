package com.sdrak.netcore.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncLink;
import com.sdrak.netcore.udp.io.send.USendReqSession;

public class UdpLink<E extends NetClient<? extends UdpLink<E>>> extends SyncLink<E>
{
	private final DatagramChannel _udpChannel;

	private long _sessionId;
	
//	private long _remoteSessionId;
	
	public UdpLink(final NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
		
		
		_udpChannel = createDatagramChannel(socketAddress);
		
		sendPacket(new USendReqSession<E>());
	}
	
	private DatagramChannel createDatagramChannel(final InetSocketAddress socketAddress)
	{
		try
		{
			final DatagramChannel udpChannel = DatagramChannel.open();
			udpChannel.connect(socketAddress);
			return udpChannel;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void setSessionId(final long sessionId)
	{
		if (sessionId != 0)
			throw new RuntimeException("Connection " + this + " has sessionId already!");
		_sessionId = sessionId;
	}
	
	public long getSessionId()
	{
		return _sessionId;
	}
	
	@Override
	public void connect()
	{
		super.connect();
		sendPacket(new USendReqSession<E>());
	}

	@Override
	protected ByteBuffer read(int begin, int end) throws IOException 
	{
//		byte[] buffer = new byte[end - begin];
		final ByteBuffer dataBuffer = ByteBuffer.allocate(end - begin);
		_udpChannel.receive(dataBuffer);
//		_udpSocket.receive(udpPacket);
		return dataBuffer;
	}

	@Override
	protected void write(ByteBuffer dataBuffer, int begin, int end) throws IOException
	{
//		final DatagramPacket udpPacket = new DatagramPacket(data, begin, end);
//		_udpSocket.send(udpPacket);
		_udpChannel.send(dataBuffer, _socketAddress);
	}

	@Override
	public void write() throws IOException
	{
		
	}
	
	@Override
	protected void writeHeaders(final ByteBuffer dataBuffer, byte[] data)
	{
		super.writeHeaders(dataBuffer, data);
		dataBuffer.putLong(_sessionId);
	}
	
	@Override
	public int getHeaderSize()
	{
		return super.getHeaderSize() + 
				Long.BYTES;
	}
}
