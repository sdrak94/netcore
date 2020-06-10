package netcore.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import org.omg.CORBA.ServerRequest;

import sdrak.netcore.io.client.NetClient;

public class LoginManager 
{
	public final static String GET_ACCOUNT = "SELECT * FROM accounts WHERE login_name = ?";
	public final static String GET_USER    = "SELECT user_id FROM users WHERE login_name = ?";
	
	public final static String CRT_ACCOUNT = "INSERT INTO accounts VALUES (?,?,?)";
	
	public synchronized static void requestLogin(String loginName, String passhash, NetClient<?> client)
	{
		try(final Connection con = DatabaseFactory.getInstance().getConnection(); final PreparedStatement st = con.prepareStatement(GET_ACCOUNT))
		{	st.setString(1, loginName);
			try (final ResultSet rs = st.executeQuery())
			{	if (rs.next())
				{	if (passhash.equals(rs.getString("password_hash")))//login succesfull, check if already online...
					{	client.setLoginName(loginName); // since loggin succeded, we are safe to attach the given username to the client that requested it
						final User activeUser = JServer.getInstance().findUser((u) -> u.getLoginName().equalsIgnoreCase(loginName));
						if (activeUser != null) // ... that user is already online, exit now and inform the activeUser!
						{	client.sendPacket(new ShowDlg("That user is already online!", "Login Failed!", JOptionPane.ERROR_MESSAGE));
							activeUser.sendPacket(new ShowDlg("Someone tried to access your account!", "Security warning!", JOptionPane.ERROR_MESSAGE));
						}
						else //search for the user in the database, if found authd that client, else require creation.
						{	try (final PreparedStatement st2 = con.prepareStatement(GET_USER))
							{	st2.setString(1, loginName);
								try (final ResultSet rs2 = st2.executeQuery())
								{	if (rs2.next()) // user found, authenticate now
									{	final User user = new User(rs2.getInt(1), loginName);
										client.authenticate(user);
									}
									else // user not found, promt creation now
									{	client.setAllowUserCreate(true);
										client.sendPacket(new ServerRequest(1));
									}
								}
							}
						}
						
					}
					else // Passwords didnt match
						client.sendPacket(new ShowDlg("Password incorrect!", "Login Failed!", JOptionPane.ERROR_MESSAGE));
				}
				else //ResultSet returned empty
					client.sendPacket(new ShowDlg("That account doesn't exist!", "Login Failed!", JOptionPane.ERROR_MESSAGE));
			}
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
	
	public synchronized static void requestCreateAccount(String loginName, String hash, ChatClientImage client)
	{	try(final Connection con = DatabaseFactory.getInstance().getConnection(); final PreparedStatement st = con.prepareStatement(GET_ACCOUNT))
		{	st.setString(1, loginName);
			try (final ResultSet rs = st.executeQuery())
			{	if (!rs.next()) // continue only if the query found no results
				{	try(PreparedStatement st2 = con.prepareStatement(CRT_ACCOUNT))
					{	st2.setString(1, loginName);
						st2.setString(2, hash);
						st2.setDate(3, new Date(System.currentTimeMillis()));
						st2.executeUpdate();
						//creation succesfull, exit this lock and invoke login with different sync priority
						DelayManager.getInstance().schedule(() -> requestLogin(loginName, hash, client), 20);
					}
				}
				else // if already exists, exit and inform the user!
				{	client.sendPacket(new ShowDlg("That username already exists!", "Create Failed", JOptionPane.ERROR_MESSAGE));
				}
			}
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
}
