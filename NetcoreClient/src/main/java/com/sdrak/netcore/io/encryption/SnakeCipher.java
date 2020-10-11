package com.sdrak.netcore.io.encryption;

public class SnakeCipher extends Cipher
{
	private final byte[] _keys;
	
	public SnakeCipher(byte ... keys)
	{
		_keys = keys;
	}
	
	@Override
	public void enc(byte[] dataBuffer) 
	{
		for (int i=0;i<dataBuffer.length;i++)
		{
			byte key = _keys[i % _keys.length];
			dataBuffer[i] = (byte) (dataBuffer[i] ^ key);
		}
	}

	@Override
	public void dec(byte[] dataBuffer) 
	{
		for (int i=0;i<dataBuffer.length;i++)
		{
			byte key = _keys[i % _keys.length];
			dataBuffer[i] = (byte) (dataBuffer[i] ^ key);
		}
	}

}
