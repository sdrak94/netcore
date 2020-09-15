package com.sdrak.netcore.io.encryption;

public class SnakeCipher extends Cipher
{
	private final byte[] _keys;
	
	public SnakeCipher(byte ... keys)
	{
		_keys = keys;
	}
	
	@Override
	public void enc(byte[] b) 
	{
		for (int i=0;i<b.length;i++)
		{
			byte key = _keys[i % _keys.length];
			b[i] = (byte) (b[i] ^ key);
		}
	}

	@Override
	public void dec(byte[] b) 
	{
		for (int i=0;i<b.length;i++)
		{
			byte key = _keys[i % _keys.length];
			b[i] = (byte) (b[i] ^ key);
		}
	}

}
