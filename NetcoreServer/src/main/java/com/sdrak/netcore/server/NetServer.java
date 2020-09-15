package com.sdrak.netcore.server;

import java.util.Collection;

import com.sdrak.netcore.Console;
import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.ReadablePacket;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.io.client.NetClient;

public abstract class NetServer<E extends NetClient<?>> implements Runnable
{
	private boolean running = true;
	
	private final Class<E> _clientClass;
	private final Collection<E> _connectedNow;
	
	public NetServer(Collection<E> connectedNow, final Class<E> clientClass)
	{
		_connectedNow = connectedNow;
		_clientClass = clientClass;
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
		while(running)
		{
			awaitAccept();
			final E client = accept();
			register(client);
		}
	}
	
	protected void awaitAccept()
	{
		Console.write("Waiting for a connection.");
		final E client = accept();
		register(client);
	}
	
	protected final E forgeClient(final NetConnection<E> connection) throws Exception
	{
		final E client = _clientClass.getConstructor(connection.getClass()).newInstance(connection);
		connection.setClient(client);
		return client;
	}
	
	protected final void register(E client)
	{
		Console.write("Client Connected: " + client.toString() + " ! IP: " + client.getIP());
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
