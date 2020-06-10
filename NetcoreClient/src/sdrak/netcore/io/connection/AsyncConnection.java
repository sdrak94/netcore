package sdrak.netcore.io.connection;

import java.io.IOException;

import sdrak.netcore.io.NetConnection;
import sdrak.netcore.io.NetworkHandler;
import sdrak.netcore.io.client.NetClient;

public class AsyncConnection<E extends NetClient<?>> extends NetConnection<E>
{
	public AsyncConnection(NetworkHandler<E> netHandler)
	{
		super(netHandler);
	}

	@Override
	protected byte[] read(int begin, int end) throws IOException
	{
		return null;
	}

	@Override
	protected void write(byte[] buffer, int begin, int end) throws IOException
	{
	}

	@Override
	public void read() throws IOException
	{
	}

	@Override
	public void write(byte[] b) throws IOException
	{
	}
}
