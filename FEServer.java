import java.util.*;
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

/*         HashMap params = new HashMap();
        HashMap seeds = new HashMap();

        for(int i = 0 ; i < args.length ; i+=2) {
            params.put(args[i], args[i+1]);
        }
        String seedString = (String)params.get("-seeds");
        String[] seedStrings = seedString.split(",");

        for(int i = 0 ; i < seedStrings.length ; i++) {
            String[] seed = seedStrings[i].split(":");
            seeds.put(seed[0], seed[1]);
        }
        params.remove("-seeds"); */

        try {
            PerfCounters counter = new PerfCounters();
			ArrayList<ServerNode> BEServers = new ArrayList<ServerNode>();
			ServerNode s = new ServerNode("localhost", 14950, 24950, 2);
			BEServers.add(s);
			ServerNode s1 = new ServerNode("localhost", 14952, 24952, 1);
			BEServers.add(s1);
            passwordHandler = new FEPasswordHandler(counter, BEServers);
            passwordProcessor = new A1Password.Processor(passwordHandler);

            managementHandler = new FEManagementHandler(counter, BEServers);
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
