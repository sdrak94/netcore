package com.sdrak.netcore.udp.io.sendable;

import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;

public class USendable<E extends NetClient<UdpLink<?>>> extends SendablePacket<E>
{
	public USendable(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void writeImpl()
	{
	}
}
