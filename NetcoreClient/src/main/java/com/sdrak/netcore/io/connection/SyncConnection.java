package com.sdrak.netcore.io.connection;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public abstract class SyncConnection<E extends NetClient<?>> extends NetConnection<E>
{
	protected final Thread _readThread;
	
	public SyncConnection(NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		super(netHandler, socketAddress);
		_readThread = new Thread(() ->
		{	
			while(connected) try
			{	
				read();
			}
			catch (Exception e)
			{	
				e.printStackTrace();
				_client.onForceExit();
			}
		});
	}
	
	protected void connect()
	{
		_readThread.start();
	}
	
	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
	

}
