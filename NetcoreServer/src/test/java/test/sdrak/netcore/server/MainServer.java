package test.sdrak.netcore.server;

import java.util.ArrayList;
import java.util.HashSet;

import com.sdrak.netcore.factory.ClientFactory;
import com.sdrak.netcore.factory.ConnectionFactory;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.server.tcp.TcpChannel;
import com.sdrak.netcore.tcp.TcpConnection;

import test.sdrak.netcore.server.io.recv.PingRecv;
import test.sdrak.netcore.server.io.recv.PongRecv;
import test.sdrak.netcore.server.io.send.PingSend;
import test.sdrak.netcore.tcp.TClient;
import test.sdrak.netcore.tcp.TServerClient;

public class MainServer
{
	public static void main(String[] args) throws Exception
	{
		{   ///////// SERVER /////////
			final HashSet<TServerClient> aliveClients = new HashSet<>();
			
			final var channel = new TcpChannel<>(aliveClients, TServerClient::new, 4000);

			channel.register(ClientState.NO_AUTHD, TestNetDictionary.PING_RECV, PingRecv::new);
			
			final Thread thread = new Thread(channel);
			thread.start();
		}
		


		{	///////// CLIENT //////////
			
			
			final ArrayList<TClient> clients = new ArrayList<>(1500);
			final ClientFactory<TClient, TcpConnection<TClient>> clientFactory = new ClientFactory<>(TClient::new);
			final ConnectionFactory<TcpConnection<TClient>, TClient> connectionFactory = new ConnectionFactory<>(TcpConnection::new);
			connectionFactory.register(ClientState.NO_AUTHD, TestNetDictionary.PONG_RECV, PongRecv::new);
			
			for (int i = 0; i < 1; i++)
			{
				final TClient client = clientFactory.create(connectionFactory.create("localhost", 4000));
				
				clients.add(client);
			}
			
			for (var client : clients)
			{
				for (int i = 0; i < 1; i++)
					client.getConnection().sendPacket(new PingSend<>(i));
			}
		}
		
	}
}
