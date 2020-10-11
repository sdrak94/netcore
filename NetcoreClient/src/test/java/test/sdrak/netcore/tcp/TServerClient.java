package test.sdrak.netcore.tcp;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.tcp.TcpLink;

public class TServerClient extends NetClient<TcpLink<TServerClient>>
{
	public TServerClient(TcpLink<TServerClient> con)
	{
		super(con);
	}

	static int c;
	int cc = c++;
	
	public String toString()
	{
		return String.format("[Server]%08d", cc);
	}
}
