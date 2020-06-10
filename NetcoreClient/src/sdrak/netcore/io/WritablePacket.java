package sdrak.netcore.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import sdrak.netcore.io.client.NetClient;

public abstract class WritablePacket<E extends NetClient<?>> implements IPacket
{
	private int packetSize;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(malloc()).order(ByteOrder.LITTLE_ENDIAN);
	
	private E _client;
	
	public abstract void writeImpl();
	
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
	{	byteBuffer.put((byte) data);
		packetSize++;
	}

	protected void writeF(double value)
	{	byteBuffer.putDouble(value);
		packetSize += 8;
	}

	protected void writeD(int value)
	{	byteBuffer.putInt(value);
		packetSize += 4;
	}

	protected void writeQ(long value)
	{	byteBuffer.putLong(value);
		packetSize += 8;
	}

	protected void writeS(String str)
	{	
		final byte[] data = str.getBytes();
		byteBuffer.putInt(data.length);
		if (data.length > 0)
			byteBuffer.put(data);
		packetSize += data.length + 4;
	}
	
	protected void writeB(byte[] data)
	{	byteBuffer.putInt(data.length);
		if (data.length > 0)
			byteBuffer.put(data);
		packetSize += data.length + 4;
	}
	
	public byte[] getData()
	{	byte[] data = new byte[packetSize];
		byteBuffer.clear();
		byteBuffer.get(data);
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
	{	byteBuffer.rewind();
		_client = null; // this packet goes idle, if used accidetantly throw an npe to inform the developer
	}
}
