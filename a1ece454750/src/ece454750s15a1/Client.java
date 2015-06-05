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

    private static A1Password.Client m_passwordService;
    private static A1Management.Client m_managementService;

    private static TTransport m_passwordTransport;
    private static TTransport m_managementTransport;

    public static void main(String [] args) {
        try {
			HashMap<String, String> params = new HashMap<String, String>();
			for(int i = 0 ; i < args.length ; i+=2) {
				params.put(args[i], args[i+1]);
			}
			String addr = params.get("-host");
			int pport = Integer.parseInt(params.get("-pport"));
			int mport = Integer.parseInt(params.get("-mport"));
            m_passwordService = connectPasswordService(addr, pport);
            m_managementService = connectManagementService(addr, mport);

            perform();

            m_passwordTransport.close();
            m_managementTransport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static A1Password.Client connectPasswordService(String addr, int port) throws TException
    {
        m_passwordTransport = new TSocket(addr, port);
        m_passwordTransport.open();

        TProtocol protocol = new  TBinaryProtocol(m_passwordTransport);
        return new A1Password.Client(protocol);
    }

    private static A1Management.Client connectManagementService(String addr, int port) throws TException
    {
        m_managementTransport = new TSocket(addr, port);
        m_managementTransport.open();

        TProtocol protocol = new  TBinaryProtocol(m_managementTransport);
        return new A1Management.Client(protocol);
    }

    private static void perform() throws TException
    {
		while(true){
			try {
				System.out.print("Enter function:");
				String input = System.console().readLine();
				String hash = null;
				if(input.equals("h"))
				{
					System.out.println("hashing.......");
					System.out.println(hashPassword("password123", 15));
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
				x.printStackTrace();
			}
		}
    }

    private static String hashPassword(String password, int logRounds) throws TException
    {
		System.out.println("hashing with log round of " + logRounds + "...");
        return m_passwordService.hashPassword(password, (short)logRounds);
    }

    private static boolean checkPassword(String password, String hash) throws TException
    {
        return m_passwordService.checkPassword(password, hash);
    }

    private static PerfCounters getPerfCounters(int port) throws TException
    {
		TTransport transport = new TSocket("localhost", port);
        transport.open();

        TProtocol protocol = new  TBinaryProtocol(transport);
        A1Management.Client client = new A1Management.Client(protocol);
        PerfCounters pc = client.getPerfCounters();
		transport.close();
		return pc;
    }
    
    private static void printGroupMembers() throws TException
    {
        List<String> ids = m_managementService.getGroupMembers();
        for(int i = 0; i < ids.size(); i++)
        {
            System.out.println(ids.get(i) + ", ");
        }
    }
	
	private static void printAllBEServers() throws TException
    {
        List<ServerNode> servers = m_managementService.getAllBEServerNodes();
		System.out.println("BE===============\n");
        for(int i = 0; i < servers.size(); i++)
        {
            System.out.println(servers.get(i) + "\n");
        }
    }
	
	private static void printAllFEServers() throws TException
    {
        List<ServerNode> servers = m_managementService.getAllFEServerNodes();
		System.out.println("FE===============\n");
        for(int i = 0; i < servers.size(); i++)
        {
            System.out.println(servers.get(i) + "\n");
        }
    }
    
}
