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

public abstract class NetConnection<E extends NetClient<?>> implements IAddressable
{
	protected boolean connected = false;
	protected final NetworkHandler<E> _netHandler;
	protected final InetSocketAddress _socketAddress;
	protected Cipher _cipher;
	protected E _client;

	public NetConnection(final NetworkHandler<E> netHandler, final InetSocketAddress socketAddress)
	{
		_netHandler = netHandler;
		_socketAddress = socketAddress;
		_cipher = DEFAULT_CIPHER;
	}

	protected abstract ByteBuffer read(int begin, int end) throws IOException;
	protected abstract void write(final ByteBuffer data, int begin, int end) throws IOException;
	
	public void connect()
	{
		if (connected)
			throw new RuntimeException(this + " is already connected!");
		
		connected = true;
	}

	public void disconnect()
	{
		if (!connected)
			throw new RuntimeException(this + " is already disconnected!");
		
		connected = false;
		if (_client != null)
			_client.onDisconnect();
		_client = null;
	}
	
	public boolean isConnected()
	{
		return connected;
	}

	public final void read() throws IOException
	{
		final ByteBuffer sizeBuffer = read(0, 4);
		sizeBuffer.rewind();
		final int size = sizeBuffer.getInt();
		final ByteBuffer buffer = read(0, size);
		buffer.flip();
		if (_cipher != null)
			_cipher.dec(buffer);
		if (_netHandler != null)
			_netHandler.decodePacket(_client, buffer);
	}

	public final void write(final ByteBuffer packetBuffer, final int packetSize) throws IOException
	{
		if (_cipher != null)
			_cipher.enc(packetBuffer);
		
		final int writeSize = getHeaderSize() + packetSize;
		
		final ByteBuffer dataBuffer = ByteBuffer.allocate(writeSize);

		writeHeader(dataBuffer, packetBuffer, packetSize);
		writeBuffer(dataBuffer, packetBuffer);
		write(dataBuffer, 0, writeSize);
	}
	
	private void writeBuffer(final ByteBuffer destination, final ByteBuffer dataBuffer)
	{
		dataBuffer.flip();
		destination.put(dataBuffer);
		destination.flip();
	}

	protected void writeHeader(final ByteBuffer dataBuffer, final ByteBuffer packetBuffer, final int packetSize)
	{
		dataBuffer.putInt(packetSize);
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
		{
			writePacket.write();
			write(writePacket.getDataBuffer(), writePacket.getDataSize());
			writePacket.reset(); // reset for future re-use
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Asynchronous, won't cause network blocking. Safe to use via MainThreads
	 * 
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

	public NetworkHandler<E> getNetworkHandler()
	{
		return _netHandler;
	}

	public void registerPacket(byte opcode, Supplier<? extends RecievablePacket<E>> rpacketClass)
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
