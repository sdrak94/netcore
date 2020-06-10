package sdrak.netcore.io;

import static sdrak.netcore.Config.DEFAULT_CIPHER;

import java.io.IOException;
import java.util.concurrent.Executors;

import sdrak.netcore.io.client.NetClient;
import sdrak.netcore.io.encryption.Cipher;

public abstract class NetConnection<E extends NetClient<?>>
{
	protected boolean connected = true;
	protected final NetworkHandler<E> _netHandler;
	protected Cipher _cipher;
	protected E _client;
	
	public NetConnection(NetworkHandler<E> netHandler)
	{
		_netHandler = netHandler;
		_cipher = DEFAULT_CIPHER;
	}
	
	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
	
	protected abstract void read() throws IOException;
	protected abstract void write(byte[] b) throws IOException;
	
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
}
