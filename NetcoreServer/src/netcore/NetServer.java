package netcore;

import java.util.Collection;

import sdrak.netcore.Console;
import sdrak.netcore.io.NetworkHandler;
import sdrak.netcore.io.ReadablePacket;
import sdrak.netcore.io.client.ClientState;
import sdrak.netcore.io.client.NetClient;

public abstract class NetServer<E extends NetClient<?>> implements Runnable
{
	private final Collection<E> _connectedNow;
	
	public NetServer(Collection<E> connectedNow)
	{
		_connectedNow = connectedNow;
	}
	
	protected final NetworkHandler<E> _netHandler = new NetworkHandler<>();
	
	public void register(int opcode, Class<? extends ReadablePacket<E>> rpacketClass)
	{
		_netHandler.register(ClientState.ONLINE, opcode, rpacketClass);
	}
	
	public void register(final ClientState state, int opcode, Class<? extends ReadablePacket<E>> rpacketClass)
	{
		_netHandler.register(state, opcode, rpacketClass);
	}
	
	protected abstract E accept();
	
	@Override
	public void run()
	{
		while(true)
		{
			Console.write("Waiting for a client to connect!");
			final E client = accept();
			_connectedNow.add(client);
		}
	}
	
	public E rmClient(E e)
	{
		_connectedNow.remove(e);
		return e;
	}
}
