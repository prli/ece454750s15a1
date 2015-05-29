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
  
  private A1Password.Client m_passwordService;
  private A1Management.Client m_managementService;

  public static void main(String [] args) {
    try {
      connectPasswordService("localhost", 14950);
	  connectManagementService("localhost", 24950);
	  
      perform();

      closeAllConnections();
    } catch (TException x) {
      x.printStackTrace();
    }
  }

  private static void closeAllConnections()
  {
	
  }
  
  private static A1Password.Client connectPasswordService(String addr, int port)
  {
	TTransport transport;
	transport = new TSocket(addr, port);
	transport.open();

	TProtocol protocol = new  TBinaryProtocol(transport);
	return new A1Password.Client(protocol);
  }
  
  private static A1Management.Client connectManagementService(String addr, int port)
  {
	TTransport transport;
	transport = new TSocket(addr, port);
	transport.open();

	TProtocol protocol = new  TBinaryProtocol(transport);
	return new A1Management.Client(protocol);
  }
  
  private static void perform(A1Password.Client FEserver) throws TException
  {
	try {
		hashPassword(FEserver, "password123");
	} catch (TException x) {
      x.printStackTrace();
    }
  }
  
  private static String hashPassword(A1Password.Client FEserver, String password) throws TException
  {
	return FEserver.hashPassword(password, (short)5);
  }
  
  private static boolean checkPassword(A1Password.Client FEserver, String password, String hash) throws TException
  {
	return FEserver.checkPassword(password, hash);
  }
    
  private static PerfCounters getPerfCounters(A1Password.Client FEserver)
  {
	return FEserver.getPerfCounters();
  }
  
  private static void printGroupMembers(A1Management.Client FEserver) throws TException
  {
	List<String> ids = FEserver.getGroupMembers();
	for(int i = 0; i < ids.size(); i++)
	{
		System.out.println(ids.get(i) + ", ");
	}
  }
  
}
