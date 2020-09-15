package com.sdrak.netcore.io.connection;

import java.io.IOException;
import java.net.InetAddress;

import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.NetworkHandler;
import com.sdrak.netcore.io.client.NetClient;

public abstract class SyncConnection<E extends NetClient<?>> extends NetConnection<E>
{
	protected final Thread _readThread;
	
	public SyncConnection(NetworkHandler<E> netHandler, final InetAddress inetAddress)
	{
		super(netHandler, inetAddress);
		_readThread = new Thread(() ->
		{	
			while(connected) try
			{	read();
			}
			catch (Exception e)
			{	
				_client.onForceExit();
			}
		});
		_readThread.start();
	}
	
	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
	

}
