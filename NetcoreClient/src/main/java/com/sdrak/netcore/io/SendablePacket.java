package com.sdrak.netcore.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.sdrak.netcore.io.client.NetClient;

public abstract class SendablePacket<E extends NetClient<?>> implements IPacket
{
	private final int _opcode;

	private final ByteBuffer _byteBuffer;
	
	public SendablePacket(final int opcode)
	{
		_opcode = opcode;
		_byteBuffer = ByteBuffer.allocate(malloc()).order(ByteOrder.LITTLE_ENDIAN);
	}
	
	private int packetSize;
	
	private E _client;
	
	public void write()
	{
		writeD(_opcode);
		writeImpl();
	}
	
	protected abstract void writeImpl();
	
	public int getOpcode()
	{
		return _opcode;
	}
	
	protected int malloc()
	{	return 1024;
	}
	
	public void setClient(E client)
	{	_client = client;
	}
	
	public E getClient()
	{	return _client;
	}
	
	protected void writeC(int data)
	{	_byteBuffer.put((byte) data);
		packetSize++;
	}

	protected void writeF(double value)
	{	_byteBuffer.putDouble(value);
		packetSize += 8;
	}

	protected void writeD(int value)
	{	_byteBuffer.putInt(value);
		packetSize += 4;
	}

	protected void writeQ(long value)
	{	_byteBuffer.putLong(value);
		packetSize += 8;
	}

	protected void writeS(String str)
	{	
		final byte[] data = str.getBytes();
		_byteBuffer.putInt(data.length);
		if (data.length > 0)
			_byteBuffer.put(data);
		packetSize += data.length + 4;
	}
	
	protected void writeB(byte[] data)
	{	_byteBuffer.putInt(data.length);
		if (data.length > 0)
			_byteBuffer.put(data);
		packetSize += data.length + 4;
	}
	
	public byte[] getData()
	{	byte[] data = new byte[packetSize];
		_byteBuffer.clear();
		_byteBuffer.get(data, 0, packetSize);
		return data;
	}
	
	/** Reset contributes to STATIC_PACKETS
	 *  packets that have no dynamic data
	 *  are statically stored and constantly
	 *  reused by different clients for code
	 *  efficiency.
	 *  
	 *  Reset will rewind the bytebuffer's pointer
	 *  recycling its whole data for future usage.
	 * 
	 */
	public void reset()
	{	_byteBuffer.rewind();
		_client = null; // this packet goes idle, if used accidetantly throw an npe to inform the developer
	}
}
