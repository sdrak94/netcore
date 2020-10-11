package com.sdrak.netcore.udp.io;

import com.sdrak.netcore.io.RecievablePacket;
import com.sdrak.netcore.io.client.NetClient;
import com.sdrak.netcore.udp.UdpLink;

public abstract class URecievable<E extends NetClient<? extends UdpLink<E>>> extends RecievablePacket<E>
{
}
