package com.sdrak.netcore.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.SyncLink;

public class UdpLink<E extends NetClient<?>> extends SyncLink<E>
{
	private final DatagramSocket _udpSock;
	
	public UdpLink(final NetworkHandler<E> netHandler, final int port)  throws IOException
	{
		this(netHandler, new DatagramSocket(port));
	}
	
	public UdpLink(final NetworkHandler<E> netHandler, final DatagramSocket udpSock)
	{
		super(netHandler, udpSock.getInetAddress());
		_udpSock = udpSock;
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException 
	{
		byte[] buffer = new byte[end - begin];
		final DatagramPacket udpPacket = new DatagramPacket(buffer, begin, end);
		_udpSock.send(udpPacket);
		return buffer;
	}

	@Override
	protected void write(byte[] data, int begin, int end) throws IOException
	{
		final DatagramPacket udpPacket = new DatagramPacket(data, begin, end);
		_udpSock.send(udpPacket);
	}

	@Override
	public void write() throws IOException
	{
		
	}
}
