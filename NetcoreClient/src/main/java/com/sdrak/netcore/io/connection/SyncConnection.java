package com.sdrak.netcore.io.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;

import com.sdrak.netcore.ThreadPools;
import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public abstract class SyncConnection<E extends NetClient<?>> extends NetConnection<E>
{
	protected ScheduledFuture<?> _readTask;
	
	public SyncConnection(NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
	}
	
	private void doRead()
	{
		
		while(connected) try
		{	
			read();
		}
		catch (Exception e)
		{	
			disconnect();
			e.printStackTrace();
		}
	}
	
	@Override
	public void connect()
	{
		super.connect();
		_readTask = ThreadPools.getInstance().scheduleGeneral(this::doRead);
	}
	
	@Override
	public void disconnect()
	{
		super.disconnect();
		_readTask.cancel(true);
	}
	
	protected abstract ByteBuffer read(int begin, int end) throws IOException;
	protected abstract void write(final ByteBuffer buffer, int begin, int end) throws IOException;
}
