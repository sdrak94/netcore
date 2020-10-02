package test.sdrak.netcore.udp;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;

public class UClient extends NetClient<UdpLink<?>>
{

	public UClient(UdpLink<UClient> con)
	{
		super(con);
		// TODO Auto-generated constructor stub
	}
}
