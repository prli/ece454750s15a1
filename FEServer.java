
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

// Generated code
import ece454750s15a1.*;

public class FEServer {

  public static FEPasswordHandler passwordHandler;
  public static FEManagementHandler managementHandler;
  
  public static A1Password.Processor passwordProcessor;
  public static A1Management.Processor managementProcessor;
  
  public static void main(String [] args) {
    try {
	  PerfCounters counter = new PerfCounters();
	  
      passwordHandler = new FEPasswordHandler(counter);
      passwordProcessor = new A1Password.Processor(passwordHandler);
	
	  managementHandler = new FEManagementHandler(counter);
      managementProcessor = new A1Management.Processor(managementHandler);
	  
      Runnable passwordThread = new Runnable() {
        public void run() {
          simple(passwordProcessor, 14950);
        }
      };
	  
	  Runnable ManagementThread = new Runnable() {
        public void run() {
          simple(managementProcessor, 24950);
        }
      };

      new Thread(passwordThread).start();
	  new Thread(ManagementThread).start();
	  
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public static void simple(A1Password.Processor processor, int port) {
    try {
      TServerTransport serverTransport = new TServerSocket(port);
      TServer server = new TSimpleServer(
              new Args(serverTransport).processor(processor));

      System.out.println("Starting the FE password server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void simple(A1Management.Processor processor, int port) {
    try {
      TServerTransport serverTransport = new TServerSocket(port);
      TServer server = new TSimpleServer(
              new Args(serverTransport).processor(processor));

      System.out.println("Starting the FE management server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
