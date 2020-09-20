package com.sdrak.netcore.server;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import com.sdrak.netcore.Console;
import com.sdrak.netcore.factory.ClientFactory;
import com.sdrak.netcore.interfaces.IHandler;
import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.ReadablePacket;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.io.client.NetClient;

public abstract class NetServer<E extends NetClient<C>, C extends NetConnection<E>> implements Runnable, IHandler<E>
{
	private boolean running = true;
	
	private final ClientFactory<E, C> _clientFactory;
	
	private final Collection<E> _connectedNow;
	
	public NetServer(Collection<E> connectedNow, final Function<C, E> clientFactory)
	{
		_connectedNow = connectedNow;
		_clientFactory = new ClientFactory<>(clientFactory);
	}
	
	protected final NetworkHandler<E> _netHandler = new NetworkHandler<>();
	
	@Override
	public void register(final ClientState state, int opcode, Supplier<? extends ReadablePacket<E>> rpacketClass)
	{
		_netHandler.register(state, opcode, rpacketClass);
	}
	
	protected abstract E accept();
	
	@Override
	public void run()
	{
		while(running)
		{
			awaitAccept();
		}
	}
	
	protected void awaitAccept()
	{
		Console.write("Waiting for a connection.");
		final E client = accept();
//		if (client != null)
			register(client);
	}
	
	protected final <T extends NetConnection<E>> E forgeClient(final C connection)
	{
		final E client = _clientFactory.create(connection);
		return client;
	}
	
	protected final void register(E client)
	{
		Console.write("Client Connected: " + client.toString() + " ! IP: " + client.getIP() + " " + _connectedNow.size());
		_connectedNow.add(client);
	}
	
	public E rmClient(E e)
	{
		_connectedNow.remove(e);
		return e;
	}
	
	protected void sleep(final int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		running = false;
	}
}
