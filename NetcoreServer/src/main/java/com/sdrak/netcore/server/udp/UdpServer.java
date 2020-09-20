//package com.sdrak.netcore.server.udp;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.function.Function;
//
//import com.sdrak.netcore.Console;
//import com.sdrak.netcore.io.client.NetClient;
//import com.sdrak.netcore.server.NetServer;
//import com.sdrak.netcore.udp.UdpLink;
//
//public class UdpServer<E extends NetClient<UdpLink<E>>> extends NetServer<E, UdpLink<E>>
//{
//	private final HashMap<String, HashMap<Integer, UdpLink<E>>> _knownClients = new HashMap<>();
//	
//	private final DatagramSocket _udpSock;
//	
//	public UdpServer(Collection<E> aliveClients, final Function<UdpLink<E>, E> clientFactory, int port) throws IOException
//	{
//		super(aliveClients, clientFactory);
//		Console.write("Starting the server");
//		_udpSock = new DatagramSocket(port);
//		Console.write("Server is loaded");
//	}
//	
//	@Override
//	protected final E accept()
//	{
//		try
//		{
//			final DatagramPacket udpPacket = new DatagramPacket(new byte[512], 512);
//			_udpSock.receive(udpPacket);
//			final byte[] buffer = udpPacket.getData();
//			final UdpLink<E> udpCon = getConnection(udpPacket);
//			//Console.write("Connection established! IP: " + sock.getInetAddress().toString());
//			//Console.write("Waiting for a new client to connect!");
//			return null;//tcpClient;
//		}
//		catch (Exception e)
//		{	e.printStackTrace();
//			return null;
//		}
//	}
//	
//	private UdpLink<E> getConnection(DatagramPacket udpPacket) throws IOException
//	{
//		final HashMap<Integer, UdpLink<E>> udpAddMap = _knownClients.get(udpPacket.getAddress().getHostAddress());
//		if (udpAddMap == null)
//		{
//			final HashMap<Integer, UdpLink<E>> udpPortCon= new HashMap<>();
//			final UdpLink<E> udpCon = new UdpLink<>(_netHandler, new DatagramSocket(udpPacket.getSocketAddress()));
//			udpPortCon.put(udpPacket.getPort(), udpCon);
//			_knownClients.put(udpPacket.getAddress().getHostAddress(), udpPortCon);
//			return udpCon;
//		}
//		UdpLink<E> udpCon = udpAddMap.get(udpPacket.getPort());
//		if (udpCon == null)
//		{
//			udpCon = new UdpLink<E>(_netHandler, new DatagramSocket(udpPacket.getSocketAddress()));
//			udpAddMap.put(udpPacket.getPort(), udpCon);
//		}
//		return udpCon;
//	}
//}
