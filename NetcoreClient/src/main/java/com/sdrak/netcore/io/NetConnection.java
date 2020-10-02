package com.sdrak.netcore.io;

import static com.sdrak.netcore.Config.DEFAULT_CIPHER;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import com.sdrak.netcore.interfaces.IAddressable;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.io.encryption.Cipher;
import com.sdrak.netcore.util.Util;

public abstract class NetConnection<E extends NetClient<?>> implements IAddressable
{
	protected boolean connected = true;
	protected final NetworkHandler<E> _netHandler;
	protected final InetSocketAddress _socketAddress;
	protected Cipher _cipher;
	protected E _client;
	
	private final ByteBuffer _headerBuffer;
	
	public NetConnection(final NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		_netHandler = netHandler;
		_socketAddress = socketAddress;
		_cipher = DEFAULT_CIPHER;
		
		_headerBuffer = ByteBuffer.allocate(getHeaderSize());
	}
	
	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
	
	public final void read() throws IOException
	{
		final byte[] sizeArray = read(0, 4);
		final int size = Util.readInt(sizeArray);
		final byte[] buffer = read(0, size);
		if (_cipher != null)
			_cipher.dec(buffer);
		if (_netHandler != null)
			_netHandler.decodePacket(_client, buffer);
	}
	
	public final void write(byte[] b) throws IOException
	{
		if (_cipher != null)
			_cipher.enc(b);
		final byte[] data = Util.concat(writeHeaders(b).array(), b);
		write(data, 0, data.length);
		_headerBuffer.flip();
	}
	
	protected ByteBuffer writeHeaders(byte[] data)
	{
		_headerBuffer.putInt(data.length);
		return _headerBuffer;
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
	
	public boolean sendPacket(SendablePacket<E> writePacket)
	{	
		if (!connected)
			return false;
		writePacket.setClient(_client);
		try
		{	writePacket.write();
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
	public void sendPacketEx(final SendablePacket<E> writePacket)
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
	
	public void registerPacket(byte opcode, Supplier<? extends ReadablePacket<E>> rpacketClass)
	{
		if (_netHandler != null)
			_netHandler.register(opcode, rpacketClass);
	}
	
	public int getHeaderSize()
	{
		return Integer.BYTES;
	}
	
	@Override
	public InetAddress getInetAddress()
	{
		return _socketAddress.getAddress();
	}
	
	@Override
	public String getIP()
	{
		return getInetAddress().toString();
	}
}
