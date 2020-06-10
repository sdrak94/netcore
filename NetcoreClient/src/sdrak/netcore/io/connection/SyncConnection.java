package sdrak.netcore.io.connection;

import java.io.IOException;

import sdrak.netcore.io.NetConnection;
import sdrak.netcore.io.NetworkHandler;
import sdrak.netcore.io.client.NetClient;
import sdrak.netcore.util.Util;

public abstract class SyncConnection<E extends NetClient<?>> extends NetConnection<E>
{
	protected final Thread _readThread;
	
	public SyncConnection(NetworkHandler<E> netHandler)
	{
		super(netHandler);
		_readThread = new Thread(new Runnable()
		{	@Override
			public void run()
			{	
				while(connected) try
				{	read();
				}
				catch (Exception e)
				{	
					_client.onForceExit();
				}
			}
		});
		_readThread.start();
	}
	
	protected abstract byte[] read(int begin, int end) throws IOException;
	protected abstract void write(byte[] buffer, int begin, int end) throws IOException;
	
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
}
