package test.sdrak.netcore.tcp;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.tcp.TcpLink;

public class TClient2 extends NetClient<TcpLink<TClient2>>
{
	public TClient2(TcpLink<TClient2> con)
	{
		super(con);
	}

	static int c;
	int cc = c++;
	
	public String toString()
	{
		return String.format("%08d", cc);
	}
}
