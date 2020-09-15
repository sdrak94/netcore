package com.sdrak.netcore.io;

import java.util.HashMap;

import com.sdrak.netcore.Console;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.util.Util;

public class NetworkHandler<E extends NetClient<?>>
{
	private HashMap<ClientState, HashMap<Byte, Class<? extends ReadablePacket<E>>>> classOpcodes = new HashMap<>();
	
	/**
	 * register associates a specific packet class to an IState and an opcode, then the server will search for the
	 * opcode that corresponds to this specific clientState and only then it will initialize it.
	 * 
	 * @param clientState The clientState that is allowed to run this class
	 * @param opcode the packet opcode that MUST be unique for every clientState-opcode combination
	 * @param rpacketClass the class that is initialized upon the combination is fulfilled.
	 */
	
	public final void register(final ClientState clientState, int opcode, Class<? extends ReadablePacket<E>> rpacketClass)
	{
		final HashMap<Byte, Class<? extends ReadablePacket<E>>> stateMap = getStateMap(clientState);
		stateMap.put(new Byte((byte)opcode), rpacketClass);
	}
	
	public final void register(int opcode, Class<? extends ReadablePacket<E>> rpacketClass)
	{
		register(ClientState.ONLINE, opcode, rpacketClass);
	}
	
	private final HashMap<Byte, Class<? extends ReadablePacket<E>>> getStateMap(final ClientState state)
	{
		HashMap<Byte, Class<? extends ReadablePacket<E>>> stateMap = classOpcodes.get(state);
		if (stateMap == null)
		{	stateMap = new HashMap<>();
			classOpcodes.put(state, stateMap);
		}
		return stateMap;
	}
	
	/**
	 * Runs every time a client sends a packet to the server. If the packet is not known its data will be ignored.
	 * If a java class that corresponds to the packet's data is found, it will be initiated by the PacketHandler and
	 * invoke all the necessary code automatically.
	 * 
	 * @param client The actual client that transmitted this data
	 * @param data A byte array containing all the information the client transmitted to the server
	 */
	public void decodePacket(E client, byte[] data)
	{
		final byte opcode = data[0];
		final ReadablePacket<E> readPacket = getPacket(client, opcode);
		if (readPacket == null)
			Console.write("Unknown packet " + Util.hexValue(opcode) + " for ClientState: " + client.getState());
		else
			readPacket.init(client, data);
	}
	
	/**
	 * @param opcode The first byte of the packet represents the id that corresponds to each packet class
	 * @return a new instance of ReadPacket or null if the packet is invalid
	 */
	private ReadablePacket<E> getPacket(E client, byte opcode)
	{	final HashMap<Byte, Class<? extends ReadablePacket<E>>> stateMap = classOpcodes.get(client.getState());
		if (stateMap == null)
			return null;
		try
		{	return stateMap.get(new Byte(opcode)).newInstance();
		}
		catch (Exception e)
		{	return null;
		}
	}
}
