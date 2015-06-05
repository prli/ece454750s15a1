package ece454750s15a1;

import java.util.*;
import java.util.List;
import java.util.Arrays;

// Generated code
import ece454750s15a1.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class Client {

	private static String m_addr;
	private static int m_pport;
	private static int m_mport;
	
    public static void main(String [] args) {
			HashMap<String, String> params = new HashMap<String, String>();
			for(int i = 0 ; i < args.length ; i+=2) {
				params.put(args[i], args[i+1]);
			}
			m_addr = params.get("-host");
			m_pport = Integer.parseInt(params.get("-pport"));
			m_mport = Integer.parseInt(params.get("-mport"));

            perform();
    }

    private static void perform()
    {
		String hash = null;
		while(true){
			try {
				System.out.print("Enter function:");
				String input = System.console().readLine();
				if(input.equals("h"))
				{
					System.out.println("hashing.......");
					hash = hashPassword("password123", 15);
					System.out.println(hash);
				}
				else if(input.equals("c"))
				{
					System.out.println("checking.......");
					System.out.println(checkPassword("password123", hash));
				}
				else if(input.equals("a"))
				{
					printAllFEServers();
					printAllBEServers();
				}
				else if(input.equals("p"))
				{
					System.out.print("Enter port:");
					int port = Integer.parseInt(System.console().readLine());
					System.out.println(getPerfCounters(port));
				}
			} catch (TException x) {
				x.getMessage();
			}
		}
    }

    private static String hashPassword(String password, int logRounds) throws TException
    {
		System.out.println("hashing with log round of " + logRounds + "...");
		TTransport m_passwordTransport = new TSocket(m_addr, m_pport);
		try
		{
			m_passwordTransport.open();

			TProtocol protocol = new  TBinaryProtocol(m_passwordTransport);
			A1Password.Client client = new A1Password.Client(protocol);
			String hash = client.hashPassword(password, (short)logRounds);
			return hash;
		}
		catch(TException e)
		{
			e.printStackTrace();
		}
		finally
		{
			m_passwordTransport.close();
		}
		return "";
    }

    private static boolean checkPassword(String password, String hash) throws TException
    {
		System.out.println("checking password...");
		TTransport m_passwordTransport = new TSocket(m_addr, m_pport);
		try
		{
			m_passwordTransport.open();

			TProtocol protocol = new  TBinaryProtocol(m_passwordTransport);
			A1Password.Client client = new A1Password.Client(protocol);
			boolean checked = client.checkPassword(password, hash);
			return checked;
		}
		catch(TException e)
		{
			e.printStackTrace();
		}
		finally
		{
			m_passwordTransport.close();
		}
		return false;
    }

    private static PerfCounters getPerfCounters(int port) throws TException
    {
		TTransport transport = new TSocket(m_addr, m_mport);
		PerfCounters pc = null;
		try
		{
			transport.open();
			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client client = new A1Management.Client(protocol);
			pc = client.getPerfCounters();
			return pc;
		}
		catch(TException e)
		{
			e.printStackTrace();
		}
		finally
		{
			transport.close();
		}
		return pc;
    }
    
    private static void printGroupMembers() throws TException
    {
		TTransport transport = new TSocket(m_addr, m_mport);
		try
		{
			transport.open();
			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client client = new A1Management.Client(protocol);
			List<String> ids = client.getGroupMembers();
			for(int i = 0; i < ids.size(); i++)
			{
				System.out.println(ids.get(i) + ", ");
			}
		}
		catch(TException e)
		{
			e.printStackTrace();
		}
		finally
		{
			transport.close();
		}
    }
	
	private static void printAllBEServers() throws TException
    {
		TTransport transport = new TSocket(m_addr, m_mport);
		try
		{
			transport.open();
			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client client = new A1Management.Client(protocol);
			List<ServerNode> servers = client.getAllBEServerNodes();
			System.out.println("BE===============\n");
			for(int i = 0; i < servers.size(); i++)
			{
				System.out.println(servers.get(i) + "\n");
			}
		}
		catch(TException e)
		{
			e.printStackTrace();
		}
		finally
		{
			transport.close();
		}
    }
	
	private static void printAllFEServers() throws TException
    {
		TTransport transport = new TSocket(m_addr, m_mport);
		try
		{
			transport.open();
			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client client = new A1Management.Client(protocol);
			List<ServerNode> servers = client.getAllFEServerNodes();
			System.out.println("FE===============\n");
			for(int i = 0; i < servers.size(); i++)
			{
				System.out.println(servers.get(i) + "\n");
			}
		}
		catch(TException e)
		{
			e.printStackTrace();
		}
		finally
		{
			transport.close();
		}
    }
    
}
