package com.sdrak.netcore.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.connection.AsyncConnection;

public class UdpChannel<E extends NetClient<?>> extends AsyncConnection<E>
{
	private final DatagramSocket _udpSock;
	
	public UdpChannel(NetworkHandler<E> netHandler, DatagramSocket udpSock)
	{
		super(netHandler, null);
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
}
