import java.util.*;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

// Generated code
import ece454750s15a1.*;

public class BEServer {

    public static BEPasswordHandler passwordHandler;
    public static BEManagementHandler managementHandler;

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

            passwordHandler = new BEPasswordHandler(counter);
            passwordProcessor = new A1Password.Processor(passwordHandler);

            managementHandler = new BEManagementHandler(counter);
            managementProcessor = new A1Management.Processor(managementHandler);
			
			String addr = "";
			//int pport = 34950;
            Runnable psw = new Runnable() {
                public void run() {
                    simple(passwordProcessor, 34950);
                }
            };
			
			//int mport = 44950;
            Runnable manage = new Runnable() {
                public void run() {
                    simple(managementProcessor, 44950);
                }
            };

            new Thread(psw).start();
            new Thread(manage).start();
			
			int ncores = 2;
			joinCluster(addr, 34950, 44950, ncores);
			
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(A1Password.Processor processor, int port) {
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TServer server = new TSimpleServer(
                                               new Args(serverTransport).processor(processor));

            System.out.println("Starting the BE password server...");
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
            
            System.out.println("Starting the BE management server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void joinCluster(String addr, int pport, int mport, int ncores) throws TException
	{
		TTransport transport = new TSocket(addr, mport);
        transport.open();

        TProtocol protocol = new  TBinaryProtocol(transport);
        A1Management.Client client = new A1Management.Client(protocol);
		
		client.addServerNode(addr, pport, mport, ncores);
		transport.close();
	}
}
