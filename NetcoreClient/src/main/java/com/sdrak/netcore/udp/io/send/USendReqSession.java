package com.sdrak.netcore.udp.io.send;

import com.sdrak.netcore.NetDictionary;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;
import com.sdrak.netcore.udp.io.USendable;

public class USendReqSession<E extends NetClient<? extends UdpLink<E>>> extends USendable<E>
{

	public USendReqSession()
	{
		super(NetDictionary.U_SEND__REQ_SESSION);
	}

	@Override
	protected void writeImpl()
	{
		//no content
	}

}
