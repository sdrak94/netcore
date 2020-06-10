package sdrak.netcore.io.encryption;

public abstract class Cipher
{
	public abstract void enc(byte[] b);
	public abstract void dec(byte[] b);
}
