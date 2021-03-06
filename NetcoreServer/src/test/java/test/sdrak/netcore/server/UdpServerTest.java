package test.sdrak.netcore.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.sdrak.netcore.factory.ClientFactory;
import com.sdrak.netcore.factory.ConnectionFactory;
import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.udp.UdpChannel;
import com.sdrak.netcore.udp.UdpLink;

public class UdpServerTest
{
	public static class UClient extends NetClient<UdpLink<UClient>>
	{

		public UClient(UdpLink<UClient> con)
		{
			super(con);
		}
		
		@Override
		public String toString()
		{
			return String.format("sessionId: %016X", getConnection().getSessionId());
		}
		
	}
	
	public static void main(String[] args) throws Exception
	{
		final ArrayList<UClient> connectedClients = new ArrayList<>();
		
		final UdpChannel<UClient> udpServer = new UdpChannel<>(connectedClients, UClient::new, 1115);
		
		final Thread serverTask = new Thread(udpServer);
		serverTask.start();
		
		final ClientFactory<UClient, UdpLink<UClient>> uclientFactory = new ClientFactory<>(UClient::new);  
		final ConnectionFactory<UdpLink<UClient>, UClient> ulinkFactory = new ConnectionFactory<>(UdpLink::new);
		
		final InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1115);

		final UClient uclient0 = uclientFactory.create(ulinkFactory.create(socketAddress));
	}
	
	private static class UdpPacket extends SendablePacket<UClient>
	{
		protected UdpPacket()
		{
			super(0x01);
		}

		@Override
		public void writeImpl()
		{
			writeD(0x02);
		}
	}



}
