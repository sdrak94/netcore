package sdrak.netcore.io.client;

import sdrak.netcore.io.NetConnection;
import sdrak.netcore.io.encryption.Cipher;

public abstract class NetClient<E extends NetConnection<?>>
{
	protected final E _con;
	
	private ClientState _state = ClientState.NO_AUTHD;

	public NetClient(E con)
	{
		_con = con;
	}
	
	public E getConnection()
	{
		return _con;
	}
	
	public final void setState(ClientState state)
	{
		_state = state;
	}
	
	public ClientState getState()
	{
		return _state;
	}
	
	public void setEncryption(Cipher cipher)
	{
		_con.setEncryption(cipher);
	}
	
	public void onForceExit()
	{
		_con.destroy();
		setState(ClientState.OFFLINE);
	}
}
