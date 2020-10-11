package com.sdrak.netcore.io.client;

import java.net.InetAddress;

import com.sdrak.netcore.interfaces.IAddressable;
import com.sdrak.netcore.io.NetConnection;
import com.sdrak.netcore.io.encryption.Cipher;

public abstract class NetClient<E extends NetConnection<?>> implements IAddressable
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
	
	public void disconnect()
	{
		_con.disconnect();
	}
	
	public void onDisconnect()
	{
		setState(ClientState.OFFLINE);
	}
	
	@Override
	public InetAddress getInetAddress()
	{
		return _con.getInetAddress();
	}
	
	@Override
	public String getIP()
	{
		return _con.getIP();
	}
}
