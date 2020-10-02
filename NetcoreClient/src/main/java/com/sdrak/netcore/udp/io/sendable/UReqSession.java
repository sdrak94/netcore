package com.sdrak.netcore.udp.io.sendable;

import static com.sdrak.netcore.PacketDictionary.U__REQ_SSESSION;

import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;

public class UReqSession<E extends NetClient<UdpLink<?>>> extends USendable<E>
{
	public UReqSession(final long sessionId)
	{
		super(U__REQ_SSESSION);
	}
	
	public UReqSession()
	{
		super(U__REQ_SSESSION);
	}

	@Override
	public void writeImpl()
	{
		// no content
	}

}
