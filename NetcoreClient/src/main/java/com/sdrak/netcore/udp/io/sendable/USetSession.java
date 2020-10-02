package com.sdrak.netcore.udp.io.sendable;

import static com.sdrak.netcore.PacketDictionary.U__SET_SSESSION;

import com.sdrak.netcore.factory.IdFactory;
import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.NetClient;

public class USetSession extends SendablePacket<NetClient<?>>
{
	private final long _sessionId;
	
	public USetSession(final long sessionId)
	{
		super(U__SET_SSESSION);
		_sessionId = sessionId;
	}
	
	public USetSession()
	{
		super(U__SET_SSESSION);
		_sessionId = IdFactory.getInstance().generateSessionId();
	}

	@Override
	public void writeImpl()
	{
		writeQ(_sessionId);
	}

}
