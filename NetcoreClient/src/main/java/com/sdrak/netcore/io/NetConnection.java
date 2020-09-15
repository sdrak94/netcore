package com.sdrak.netcore.io;

import static com.sdrak.netcore.Config.DEFAULT_CIPHER;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;

import com.sdrak.netcore.interfaces.IAddressable;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.encryption.Cipher;
import com.sdrak.netcore.util.Util;

public abstract class NetConnection<E extends NetClient<?>> implements IAddressable
{
	protected boolean connected = true;
	protected final NetworkHandler<E> _netHandler;
	protected final InetAddress _inetAddress;
	protected Cipher _cipher;
	protected E _client;
	
	
	public NetConnection(final NetworkHandler<E> netHandler, final InetAddress inetAddress)
	{
		_netHandler = netHandler;
		_inetAddress = inetAddress;
		_cipher = DEFAULT_CIPHER;
	}
	
	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
	
//	protected abstract void read() throws IOException;
//	protected abstract void write(byte[] b) throws IOException;
	
	public final void read() throws IOException
	{
		final byte[] sizeArray = read(0, 4);
		final int size = Util.digitsToInt(sizeArray);
		final byte[] buffer = read(0, size);
		if (_cipher != null)
			_cipher.dec(buffer);
		_netHandler.decodePacket(_client, buffer);
	}
	
	public final void write(byte[] b) throws IOException
	{
		if (_cipher != null)
			_cipher.enc(b);
		final int size = b.length;
		final byte[] buffer = Util.toDigitsArray(size);
		final byte[] data = Util.concat(buffer, b);
		write(data, 0, data.length);
	}
	
	public void setEncryption(Cipher cipher)
	{
		_cipher = cipher;
	}
	
	public void setClient(E client)
	{
		_client = client;
	}
	
	public E getClient()
	{
		return _client;
	}
	
	public boolean sendPacket(WritablePacket<E> writePacket)
	{	
		if (!connected)
			return false;
		writePacket.setClient(_client);
		try
		{	writePacket.writeImpl();
			write(writePacket.getData());
			writePacket.reset(); //reset for future re-use
			return true;
		}
		catch (Exception e)
		{	e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Asynchronous, won't cause network blocking. Safe to use via MainThreads
	 * @param writePacket
	 */
	public void sendPacketEx(final WritablePacket<E> writePacket)
	{
		Executors.newSingleThreadExecutor().submit(new Runnable()
		{
			@Override
			public void run()
			{
				sendPacket(writePacket);
			}
		});
	}
	
	public boolean isAlive()
	{
		return connected;
	}
	
	public void destroy()
	{
		connected = false;
	}
	
	public NetworkHandler<E> getNetworkHandler()
	{
		return _netHandler;
	}
	
	public void registerPacket(byte opcode, Class<? extends ReadablePacket<E>> rpacketClass)
	{
		_netHandler.register(opcode, rpacketClass);
	}
	
	@Override
	public InetAddress getInetAddress()
	{
		return _inetAddress;
	}
	
	@Override
	public String getIP()
	{
		return _inetAddress.toString();
	}
}
