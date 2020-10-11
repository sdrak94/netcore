package com.sdrak.netcore;

import com.sdrak.netcore.io.encryption.Cipher;
import com.sdrak.netcore.io.encryption.SnakeCipher;

public class Config
{
//	public static final Cipher DEFAULT_CIPHER = new SnakeCipher((byte)0xFF, (byte)0xAF, (byte)0x6C);
	public static final Cipher DEFAULT_CIPHER = new SnakeCipher((byte)0x00);
}
