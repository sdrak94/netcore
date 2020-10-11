package test.sdrak.netcore.server.io.send;

import com.sdrak.netcore.io.SendablePacket;
import com.sdrak.netcore.io.client.NetClient;

import test.sdrak.netcore.server.TestNetDictionary;

public class PingSend<E extends NetClient<?>> extends SendablePacket<E>
{
	final int _pingId;
	
	public PingSend(final int pingId)
	{
		super(TestNetDictionary.PING_SEND);
		_pingId = pingId;
	}

	@Override
	protected void writeImpl()
	{
		System.out.println(getClient() + " Sending ping!");
		writeD(_pingId);
	}

}
