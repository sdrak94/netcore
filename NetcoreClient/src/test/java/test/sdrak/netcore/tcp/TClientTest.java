package test.sdrak.netcore.tcp;

import java.net.InetAddress;
import java.net.Socket;

import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.tcp.TcpConnection;

public class TClientTest
{

	public static void main(String[] args) throws Exception
	{
		final long t0 = System.currentTimeMillis();
		
		var add = InetAddress.getByName("localhost");
		
		int loops = 10;
		while (loops--> 0 )
		{

//			final TClient client = new TClient(new TcpConnection<>(createHandler(), new Socket(add, 5055)));
		}
		
		final long t1 = System.currentTimeMillis();
		
		System.out.println(t1 - t0);
	}

	private static NetworkHandler<TClient> createHandler()
	{	
		final NetworkHandler<TClient> handler = new NetworkHandler<TClient>();
		return handler;
	}
}
