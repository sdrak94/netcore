package com.sdrak.netcore.udp.readable;

import com.sdrak.netcore.io.ReadablePacket;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;

public class UGetSession<E extends NetClient<UdpLink<?>>> extends ReadablePacket<E>
{
	private long _sessionId;
	
	@Override
	protected void readImpl()
	{
		_sessionId = readQ();
	}	

	@Override
	protected void runImpl()
	{
		getClient().getConnection().setSessionId(_sessionId);
		System.out.println(String.format("Recieved sessionId: %", _sessionId));
	}

}
