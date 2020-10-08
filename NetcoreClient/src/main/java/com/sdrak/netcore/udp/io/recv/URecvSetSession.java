package com.sdrak.netcore.udp.io.recv;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;
import com.sdrak.netcore.udp.io.URecievable;

public class URecvSetSession<E extends NetClient<? extends UdpLink<E>>> extends URecievable<E>
{
	private long newSessionId;
	
	@Override
	protected void readImpl()
	{
		newSessionId = readQ();
	}

	@Override
	protected void runImpl()
	{
		System.out.printf("Setting sessionId to: %016X\n", newSessionId);
		final E c = getClient();
		c.getConnection().setSessionId(newSessionId);
	}

}
