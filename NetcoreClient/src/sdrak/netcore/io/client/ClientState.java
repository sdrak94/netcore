package sdrak.netcore.io.client;

public abstract class ClientState
{
	/** The client is new and not trusted */
	public static final ClientState NO_AUTHD = new ClientState("NO_AUTHD"){};
	/** Online normally, user && account authenticated. */
	public static final ClientState ONLINE = new ClientState("ONLINE"){};
	/** The client is disconnected. */
	public static final ClientState OFFLINE = new ClientState("OFFLINE"){};
	
	private final String _toString;
	
	public ClientState(String toString)
	{
		_toString = toString;
	}
	
	@Override
	public String toString()
	{
		return _toString;
	}
}
