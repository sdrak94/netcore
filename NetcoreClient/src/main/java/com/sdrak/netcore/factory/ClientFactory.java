package com.sdrak.netcore.factory;

import java.util.function.Function;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public class ClientFactory<E extends NetClient<C>, C extends NetConnection<E>>
{
	private final Function<C, E> _clientFactory;
	
	public ClientFactory(final Function<C, E> clientFactory)
	{
		_clientFactory = clientFactory;
	}
	
	public ClientFactory(final Function<C, E> clientFactory, final NetworkHandler<E> networkHandler)
	{
		_clientFactory = clientFactory;
	}
	
	public E create(final C con)
	{
		final E client = _clientFactory.apply(con);
		con.setClient(client);
		con.connect();
		return client;
	}
	

//	
//	private class ClientFactoryProps
//	{
//		
//	}
}
