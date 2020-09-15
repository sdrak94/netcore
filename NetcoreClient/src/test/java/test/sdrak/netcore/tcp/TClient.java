package test.sdrak.netcore.tcp;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.tcp.TcpConnection;

public class TClient extends NetClient<TcpConnection<TClient>>
{
	public TClient(TcpConnection<TClient> con)
	{
		super(con);
	}
}
