package com.sdrak.netcore.io;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.sdrak.netcore.Console;
import com.sdrak.netcore.io.client.NetClient;

public abstract class RecievablePacket<E extends NetClient<?>> implements IPacket
{
	private ByteBuffer _byteBuffer;
	private E _client;

	public RecievablePacket()
	{

	}

	public boolean init(E client, final ByteBuffer dataBuffer)
	{
//		_byteBuffer = ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN);
		_byteBuffer = dataBuffer;
		_client = client;
//		readD(); // opcode
		try
		{
			readImpl();
			runImpl();
			return true;
		}
		catch (BufferOverflowException | BufferUnderflowException e)
		{
			Console.write("Invalid packet format from " + client);
			e.printStackTrace();
			onException();
			return false;
		}
		catch (Exception e) // the issue is server sided here, we shouldn't have npe etc as far as the
							// packet is valid
		{
			Console.write("SEVERE : [PACKET PROCESSING FAILED " + getClass().getName().toUpperCase() + " + ] <=========  FIX ME");
			e.printStackTrace();
			return false;
		}
	}

	protected abstract void readImpl();

	protected abstract void runImpl();

	protected void onException()
	{
	}

	public boolean hasRemaining()
	{
		return _byteBuffer.hasRemaining();
	}

	public E getClient()
	{
		return _client;
	}

	protected final byte readC()
	{
		return _byteBuffer.get();
	}

	protected final double readF()
	{
		return _byteBuffer.getDouble();
	}

	protected final int readD()
	{
		return _byteBuffer.getInt();
	}

	protected final long readQ()
	{
		return _byteBuffer.getLong();
	}

	public final String readS()
	{
		final int size = _byteBuffer.getInt();
		final byte[] buffer = new byte[size];
		for (int i = 0; i < size; i++)
			buffer[i] = _byteBuffer.get();
		return new String(buffer);
	}

	public final byte[] readB()
	{
		final int size = _byteBuffer.getInt();
		final byte[] buffer = new byte[size];
		for (int i = 0; i < size; i++)
			buffer[i] = _byteBuffer.get();
		return buffer;
	}

	protected int remaining()
	{
		return _byteBuffer.remaining();
	}

	protected int position()
	{
		return _byteBuffer.position();
	}
}
