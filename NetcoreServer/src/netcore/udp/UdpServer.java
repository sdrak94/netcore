package netcore.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Collection;
import java.util.HashMap;

import netcore.NetServer;
import sdrak.netcore.Console;
import sdrak.netcore.io.client.NetClient;
import sdrak.netcore.udp.UdpConnection;

public class UdpServer<E extends NetClient<UdpConnection<E>>> extends NetServer<E>
{
	private final HashMap<String, HashMap<Integer, UdpConnection<E>>> _knownClients = new HashMap<>();
	
	private final Class<E> _clientClass;
	private final DatagramSocket _udpSock;
	
	public UdpServer(Collection<E> aliveClients, Class<E> clientClass, int port) throws IOException
	{
		super(aliveClients);
		Console.write("Starting the server");
		_clientClass = clientClass;
		_udpSock = new DatagramSocket(port);
		Console.write("Server is loaded");
	}
	
	@Override
	protected final E accept()
	{
		try
		{
			final DatagramPacket udpPacket = new DatagramPacket(new byte[512], 512);
			_udpSock.receive(udpPacket);
			final byte[] buffer = udpPacket.getData();
			final UdpConnection<E> udpCon = getConnection(udpPacket);
			//Console.write("Connection established! IP: " + sock.getInetAddress().toString());
			//Console.write("Waiting for a new client to connect!");
			return null;//tcpClient;
		}
		catch (Exception e)
		{	e.printStackTrace();
			return null;
		}
	}
	
	private UdpConnection<E> getConnection(DatagramPacket udpPacket) throws IOException
	{
		final HashMap<Integer, UdpConnection<E>> udpAddMap = _knownClients.get(udpPacket.getAddress().getHostAddress());
		if (udpAddMap == null)
		{
			final HashMap<Integer, UdpConnection<E>> udpPortCon= new HashMap<>();
			final UdpConnection<E> udpCon = new UdpConnection<>(_netHandler, new DatagramSocket(udpPacket.getSocketAddress()));
			udpPortCon.put(udpPacket.getPort(), udpCon);
			_knownClients.put(udpPacket.getAddress().getHostAddress(), udpPortCon);
			return udpCon;
		}
		UdpConnection<E> udpCon = udpAddMap.get(udpPacket.getPort());
		if (udpCon == null)
		{
			udpCon = new UdpConnection<E>(_netHandler, new DatagramSocket(udpPacket.getSocketAddress()));
			udpAddMap.put(udpPacket.getPort(), udpCon);
		}
		return udpCon;
	}
}
