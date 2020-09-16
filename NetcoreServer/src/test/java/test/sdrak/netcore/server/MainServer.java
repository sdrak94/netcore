package test.sdrak.netcore.server;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import com.sdrak.netcore.factory.ClientFactory;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.ReadablePacket;
import com.sdrak.netcore.io.WritablePacket;
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
		
		class DummyWrite extends WritablePacket<TClient>
		{
			int _id;
			
			public DummyWrite(int id)
			{
				_id = id;
			}
			
			@Override
			public void writeImpl()
			{
				writeD(0x6050403);
				writeD(_id);
			}
		}
		
		class DummyWrite2 extends WritablePacket<TClient2>
		{
			int _id;
			
			public DummyWrite2(int id)
			{
				_id = id;
			}
			
			@Override
			public void writeImpl()
			{
				writeD(0x6050403);
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
//					System.out.println(getClient() + " -> " + val + " -> Server");
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
//					System.out.println("Server -> " + val + " -> " + getClient());
					if (val < 10)
					getClient().getConnection().sendPacket(new DummyWrite(val));
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
		
		var add = InetAddress.getByName("localhost");

		final ArrayList<TClient> clients = new ArrayList<>(1500);
		
		final ClientFactory<TClient, TcpConnection<TClient>> asd = new ClientFactory<>();
		
		final var handler = createHandler();
		handler.register(ClientState.NO_AUTHD, 0x3040506, DummyPacket::new);
		
		for (int i = 0; i < 100; i++)
		{
			final TClient client = asd.create(TClient::new, new TcpConnection<>(handler, new Socket(add, 4000)));
			
			clients.add(client);
		}
		
		final long t0 = System.currentTimeMillis();
		
		
		for (var client : clients)
		{
			for (int i = 0; i < 10; i++)
				client.getConnection().sendPacket(new DummyWrite(i));
		}
		
		final long t1 = System.currentTimeMillis();
		
		System.out.print("end " + (t1 - t0) + " ms");
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
