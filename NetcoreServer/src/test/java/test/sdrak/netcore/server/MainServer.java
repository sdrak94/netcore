package test.sdrak.netcore.server;

import java.util.ArrayList;
import java.util.HashSet;

import com.sdrak.netcore.factory.ClientFactory;
import com.sdrak.netcore.factory.ConnectionFactory;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.ReadablePacket;
import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.server.tcp.TcpChannel;
import com.sdrak.netcore.tcp.TcpConnection;

import test.sdrak.netcore.tcp.TClient;
import test.sdrak.netcore.tcp.TClient2;

public class MainServer
{
	public static void main(String[] args) throws Exception
	{
		final HashSet<TClient2> aliveClients = new HashSet<>();
		
		class DummyWrite extends SendablePacket<TClient>
		{
			int _id;
			
			public DummyWrite(int id)
			{
				super(0x3040506);
				_id = id;
			}
			
			@Override
			public void writeImpl()
			{
				writeD(_id);
			}
		}
		
		class DummyWrite2 extends SendablePacket<TClient2>
		{
			int _id;
			
			public DummyWrite2(int id)
			{
				super(0x3040506);
				_id = id;
			}
			
			@Override
			public void writeImpl()
			{
				writeD(_id);
			}
		}
		
		class DummyPacket2 extends ReadablePacket<TClient2>
		{
			int val;
			
			@Override
			protected void readImpl() 
			{
				val = readD();
			}

			@Override
			protected void runImpl() 
			{
				try
				{
					System.out.println(getClient() + " -> " + val + " -> Server");
					if (val < 10)
					getClient().getConnection().sendPacket(new DummyWrite2(val));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		class DummyPacket extends ReadablePacket<TClient>
		{
			int val;
			
			@Override
			protected void readImpl() 
			{
				val = readD();
			}

			@Override
			protected void runImpl() 
			{
				try
				{
					System.out.println("Server -> " + val + " -> " + getClient());
					if (val < 10)
					getClient().getConnection().sendPacket(new DummyWrite(val+1));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		final var channel = new TcpChannel<>(aliveClients, TClient2::new, 4000);

		channel.register(ClientState.NO_AUTHD, 0x3040506, DummyPacket2::new);
		
		final Thread thread = new Thread(channel);
		thread.start();
		

		final ArrayList<TClient> clients = new ArrayList<>(1500);
		
		final ClientFactory<TClient, TcpConnection<TClient>> clientFactory = new ClientFactory<>(TClient::new);
		final ConnectionFactory<TcpConnection<TClient>, TClient> connectionFactory = new ConnectionFactory<>(TcpConnection::new);
		
		connectionFactory.register(ClientState.NO_AUTHD, 0x3040506, DummyPacket::new);
		
		Thread.sleep(100);
		
		for (int i = 0; i < 10; i++)
		{
			final TClient client = clientFactory.create(connectionFactory.create("localhost", 4000));
			
			clients.add(client);
		}
		
		
		final long t0 = System.currentTimeMillis();
		
		for (var client : clients)
		{
			for (int i = 0; i < 2; i++)
				client.getConnection().sendPacket(new DummyWrite(i));
		}
		
		final long t1 = System.currentTimeMillis();
		
	}
	
	private static NetworkHandler<TClient> createHandler()
	{	
		final NetworkHandler<TClient> handler = new NetworkHandler<>();
		return handler;
	}
	
	private static NetworkHandler<TClient2> createHandler2()
	{	
		final NetworkHandler<TClient2> handler = new NetworkHandler<>();
		return handler;
	}
}
