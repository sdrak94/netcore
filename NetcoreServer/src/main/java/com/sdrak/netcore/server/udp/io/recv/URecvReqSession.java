package com.sdrak.netcore.server.udp.io.recv;

import com.sdrak.netcore.factory.IdFactory;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.server.udp.io.send.USendSetSession;
import com.sdrak.netcore.udp.UdpLink;
import com.sdrak.netcore.udp.io.URecievable;

public class URecvReqSession<E extends NetClient<? extends UdpLink<E>>> extends URecievable<E>
{

	@Override
	protected void readImpl()
	{
		//no content
	}

	@Override
	protected void runImpl()
	{
		final long newSessionId = IdFactory.getInstance().generateSessionId();
		System.out.printf("Answering with new generated sessionId: %016X\n", newSessionId);
		final USendSetSession<E> usendSession = new USendSetSession<>(newSessionId);
		getClient().getConnection().sendPacket(usendSession);
	}

}
