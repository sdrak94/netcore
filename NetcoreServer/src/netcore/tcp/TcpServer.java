package netcore.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import netcore.NetServer;
import sdrak.netcore.Console;
import sdrak.netcore.io.client.NetClient;
import sdrak.netcore.tcp.TcpConnection;

public class TcpServer<E extends NetClient<TcpConnection<E>>> extends NetServer<E>
{
	private final Class<E> _clientClass;
	private final ServerSocket _serverSocket;
	
	public TcpServer(Collection<E> aliveClients, Class<E> clientClass, int port) throws IOException
	{
		super(aliveClients);
		_clientClass = clientClass;
		_serverSocket = new ServerSocket(port);
	}
	
	@Override
	protected final E accept()
	{
		try
		{	
			final Socket sock = _serverSocket.accept();
			final TcpConnection<E> tcpCon = new TcpConnection<>(_netHandler, sock);
			final E tcpClient = _clientClass.getConstructor(TcpConnection.class).newInstance(tcpCon);
			tcpCon.setClient(tcpClient);
			Console.write("Connection established! IP: " + sock.getInetAddress().toString());
			return tcpClient;
		}
		catch (Exception e)
		{	e.printStackTrace();
			return null;
		}
	}
}
