package test.sdrak.netcore.server.io.send;

import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.NetClient;

import test.sdrak.netcore.server.TestNetDictionary;

public class PongSend<E extends NetClient<?>> extends SendablePacket<E>
{
	final int _pongId;
	
	public PongSend(final int pongId)
	{
		super(TestNetDictionary.PONG_SEND);
		_pongId = pongId;
	}

	@Override
	protected void writeImpl()
	{
		System.out.println(getClient() + " Sending pong!");
		writeD(_pongId);
	}

}
