package test.sdrak.netcore.server.io.recv;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.RecievablePacket;
import com.sdrak.netcore.io.client.NetClient;

import test.sdrak.netcore.server.io.send.PingSend;

public class PongRecv<E extends NetClient<? extends NetConnection<E>>> extends RecievablePacket<E>
{
	private int pongId;

	@Override
	protected void readImpl()
	{
		pongId = readD();
	}

	@Override
	protected void runImpl()
	{
		final E client = getClient();
		System.out.println(getClient() + " Pong recieved: " + pongId + "!");
		if (pongId < 10)
			client.getConnection().sendPacket(new PingSend<E>(pongId + 1));
	}
}
