package com.sdrak.netcore.io.encryption;

public class MultiCipher extends Cipher
{
	private final Cipher[] _xorCiphers;
	
	public MultiCipher(Cipher ... xorCiphers)
	{
		_xorCiphers = xorCiphers;
	}
	
	public void enc(byte[] buffer)
	{
		for (int i=0;i<_xorCiphers.length;i++)
			_xorCiphers[i].enc(buffer);
	}
	
	public void dec(byte[] buffer)
	{
		for (int i=0;i<_xorCiphers.length;i++)
			_xorCiphers[i].enc(buffer);
	}
}
