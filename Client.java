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
            m_passwordService = connectPasswordService("localhost", 14951);
            m_managementService = connectManagementService("localhost", 24951);

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
        try {
            System.out.println(hashPassword("password123"));
            System.out.println(getPerfCounters());
			printAllBEServers();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static String hashPassword(String password) throws TException
    {
        return m_passwordService.hashPassword(password, (short)5);
    }

    private static boolean checkPassword(String password, String hash) throws TException
    {
        return m_passwordService.checkPassword(password, hash);
    }

    private static PerfCounters getPerfCounters() throws TException
    {
        return m_managementService.getPerfCounters();
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
        for(int i = 0; i < servers.size(); i++)
        {
            System.out.println(servers.get(i) + "\n");
        }
    }
    
}
