
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

  public static PasswordServiceHandler passwordHandler;
  public static ManagementServiceHandler managementHandler;
  
  public static A1Management.Processor managementProcessor;

  public static void main(String [] args) {
    try {

      managementHandler = new ManagementServiceHandler();
      managementProcessor = new A1Management.Processor(managementHandler);

      Runnable simple = new Runnable() {
        public void run() {
          simple(managementProcessor);
        }
      };

      new Thread(simple).start();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public static void simple(A1Management.Processor processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(14950);
      TServer server = new TSimpleServer(
              new Args(serverTransport).processor(processor));

      System.out.println("Starting the FE server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
