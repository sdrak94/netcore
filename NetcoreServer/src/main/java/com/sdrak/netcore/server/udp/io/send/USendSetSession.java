package com.sdrak.netcore.server.udp.io.send;

import com.sdrak.netcore.NetDictionary;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;
import com.sdrak.netcore.udp.io.USendable;

public class USendSetSession<E extends NetClient<? extends UdpLink<E>>> extends USendable<E>
{

	private final long _newSessionId;
	
	
	public USendSetSession(final long newSessionId)
	{
		super(NetDictionary.U_SEND__SET_SESSION);
		_newSessionId = newSessionId;
	}

	@Override
	protected void writeImpl()
	{
		writeQ(_newSessionId);
	}

}
