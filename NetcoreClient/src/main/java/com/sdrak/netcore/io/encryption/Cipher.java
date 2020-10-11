package com.sdrak.netcore.io.encryption;

import java.nio.ByteBuffer;

public abstract class Cipher
{
	public abstract void enc(byte[] dataBuffer);
	public abstract void dec(byte[] dataBuffer);

	public void enc(final ByteBuffer byteBuffer)
	{
		enc(byteBuffer.array());
	}

	public void dec(final ByteBuffer byteBuffer)
	{
		dec(byteBuffer.array());
	}
}
