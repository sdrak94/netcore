package com.sdrak.netcore.io;

import java.util.HashMap;
import java.util.function.Supplier;

import com.sdrak.netcore.Console;
import com.sdrak.netcore.interfaces.IHandler;
import com.sdrak.netcore.io.client.ClientState;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.util.Util;

import gnu.trove.map.hash.TIntObjectHashMap;

public class NetworkHandler<E extends NetClient<?>> implements IHandler<E>
{
	private HashMap<ClientState, TIntObjectHashMap<Supplier<? extends ReadablePacket<E>>>> classOpcodes = new HashMap<>();
	
	/**
	 * register associates a specific packet class to an IState and an opcode, then the server will search for the
	 * opcode that corresponds to this specific clientState and only then it will initialize it.
	 * 
	 * @param clientState The clientState that is allowed to run this class
	 * @param opcode the packet opcode that MUST be unique for every clientState-opcode combination
	 * @param rpacketClass the class that is initialized upon the combination is fulfilled.
	 */
	
	@Override
	public final void register(final ClientState clientState, int opcode, Supplier<? extends ReadablePacket<E>> packetFactory)
	{
		final var stateMap = getStateMap(clientState);
		stateMap.put(opcode, packetFactory);
	}
	
	private final TIntObjectHashMap<Supplier<? extends ReadablePacket<E>>> getStateMap(final ClientState state)
	{
		var stateMap = classOpcodes.get(state);
		if (stateMap == null)
		{	stateMap = new TIntObjectHashMap<>();
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
		final int opcode = Util.intRead(data);
		final ReadablePacket<E> readPacket = getPacket(client, opcode);
		if (readPacket == null)
			Console.write("Client: " + client +  " Unknown packet " + Util.hexValue(opcode) + " for ClientState: " + client.getState());
		else
			readPacket.init(client, data);
	}
	
	/**
	 * @param opcode The first byte of the packet represents the id that corresponds to each packet class
	 * @return a new instance of ReadPacket or null if the packet is invalid
	 */
	private <R extends ReadablePacket<E>> ReadablePacket<E> getPacket(E client, int opcode)
	{	final var stateMap = classOpcodes.get(client.getState());
		if (stateMap == null)
			return null;
		final var packetClass = stateMap.get(opcode);
		if (packetClass == null)
			return null;
		return packetClass.get();
	}
}
