package com.sdrak.netcore.factory;

import java.net.InetSocketAddress;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.sdrak.netcore.interfaces.IHandler;
import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.RecievablePacket;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.io.client.NetClient;

public class ConnectionFactory<E extends NetConnection<C>, C extends NetClient<E>> implements IHandler<C>
{
	private final BiFunction<NetworkHandler<C>, InetSocketAddress, E> _connectionFactory;
	private final NetworkHandler<C> _networkHandler;
	
	public ConnectionFactory(final BiFunction<NetworkHandler<C>, InetSocketAddress, E> connectionFactory)
	{
		_connectionFactory = connectionFactory;
		_networkHandler = new NetworkHandler<>();
	}
	
  	public E create(final String address, final int port)
	{
		return create(new InetSocketAddress(address, port));
	}
  	
  	public E create(final InetSocketAddress socketAddress)
  	{
  		return _connectionFactory.apply(_networkHandler, socketAddress);
  	}
	
	@Override
	public void register(final ClientState clientState, final int opcode, final Supplier<? extends RecievablePacket<C>> packetFactory)
	{
		_networkHandler.register(clientState, opcode, packetFactory);
	}
//	
//	private class ClientFactoryProps
//	{
//		
//	}
}
