package com.sdrak.netcore.io.encryption;

public class XORCipher extends Cipher
{
	private final byte _key;
	
	public XORCipher(byte key)
	{
		_key = key;
	}
	
	public void enc(byte[] buffer)
	{
		for (int i=0;i<buffer.length;i++)
			buffer[i] = (byte) (buffer[i] ^ _key);
	}
	
	public void dec(byte[] buffer)
	{
		for (int i=0;i<buffer.length;i++)
			buffer[i] = (byte) (buffer[i] ^ _key);
	}
}
