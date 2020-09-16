package com.sdrak.netcore.factory;

import java.util.function.Function;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.client.NetClient;

public class ClientFactory<E extends NetClient<C>, C extends NetConnection<E>>
{
	public E create(final Function<C, E> clientFactory, final C con)
	{
		final E client = clientFactory.apply(con);
		con.setClient(client);
		return client;
	}
}
