package com.sdrak.netcore.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.sdrak.netcore.io.client.NetClient;

public abstract class SendablePacket<E extends NetClient<?>> implements IPacket
{
	private final int _opcode;
	
	private int packetSize;
	private ByteBuffer byteBuffer;

	private E _client;
	
	protected SendablePacket(final int opcode)
	{
		_opcode = opcode;
		byteBuffer = ByteBuffer.allocate(malloc()).order(ByteOrder.BIG_ENDIAN);
	}

	public void write()
	{
		writeD(_opcode); //first goes the opcode
		writeImpl();
	}
	
	protected abstract void writeImpl();

	protected int malloc()
	{
		return 1024;
	}

	public void setClient(E client)
	{
		_client = client;
	}

	public E getClient()
	{
		return _client;
	}
	
	protected void writeC(int data)
	{
		byteBuffer.put((byte) data);
		packetSize++;
	}

	protected void writeF(double value)
	{
		byteBuffer.putDouble(value);
		packetSize += Double.BYTES;
	}

	protected void writeD(int value)
	{
		byteBuffer.putInt(value);
		packetSize += Integer.BYTES;
	}

	protected void writeQ(long value)
	{
		byteBuffer.putLong(value);
		packetSize += Long.BYTES;
	}

	protected void writeS(String str)
	{
		final byte[] data = str.getBytes();
		writeB(data);
	}

	protected void writeB(byte[] data)
	{
		writeD(data.length);
		if (data.length > 0)
		{
			byteBuffer.put(data);
			packetSize += data.length;
		}
	}

	public ByteBuffer getDataBuffer()
	{
		return byteBuffer;
	}
	
	public int getDataSize()
	{
		return packetSize;
	}

	/**
	 * Reset contributes to STATIC_PACKETS packets that have no dynamic data are
	 * statically stored and constantly reused by different clients for code
	 * efficiency.
	 * 
	 * Reset will rewind the bytebuffer's pointer recycling its whole data for
	 * future usage.
	 * 
	 */
	public void reset()
	{
		byteBuffer.rewind();
		packetSize = 0;
		_client = null; // this packet goes idle, if used accidetantly throw an npe to inform the
						// developer
	}
}
