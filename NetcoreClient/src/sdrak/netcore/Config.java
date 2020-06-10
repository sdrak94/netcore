package sdrak.netcore;

import sdrak.netcore.io.encryption.Cipher;
import sdrak.netcore.io.encryption.SnakeCipher;

public class Config
{
	public static final Cipher DEFAULT_CIPHER = new SnakeCipher((byte)0xFF, (byte)0xAF, (byte)0x6C);
}
