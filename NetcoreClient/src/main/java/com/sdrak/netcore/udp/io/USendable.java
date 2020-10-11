package com.sdrak.netcore.udp.io;

import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;

public abstract class USendable<E extends NetClient<? extends UdpLink<E>>> extends SendablePacket<E>
{
	protected USendable(int opcode)
	{
		super(opcode);
	}
}
