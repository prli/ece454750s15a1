package ece454750s15a1;

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
	public static class FEPasswordThread implements Runnable {
		private A1Password.Processor processor;
		private int pport;
		public FEPasswordThread(A1Password.Processor p, int port) {
		   processor = p;
		   pport = port;
		}

		public void run() {
			simple(processor, pport);
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

	public static class FEManagementThread implements Runnable {
		private A1Management.Processor processor;
		private int mport;
		public FEManagementThread(A1Management.Processor p, int port) {
			processor = p;
			mport = port;
		}

		public void run() {
			simple(processor, mport);
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
	
	public static FEPasswordHandler passwordHandler;
    public static FEManagementHandler managementHandler;

    public static A1Password.Processor passwordProcessor;
    public static A1Management.Processor managementProcessor;
	
	public static FEPasswordThread passwordThread;
	public static FEManagementThread managementThread;

    public static void main(String [] args) {
	
		String [] argLiteral = {"-host", "localhost",
            "-pport", "14950",
            "-mport", "24950",
            "-ncores","2",
            "-seeds","ecelinux1:24950,ecelinux2:24950,ecelinux3:24950"
        };

        args = argLiteral;
        
        HashMap<String, String> params = new HashMap<String, String>();
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
        params.remove("-seeds");

        System.out.println("params"+params);
        System.out.println("seeds"+seeds);

        System.err.println("------------------$$$$$$$------------");

        try {
            PerfCounters counter = new PerfCounters();
			ArrayList<ServerNode> BEServers = new ArrayList<ServerNode>();

            passwordHandler = new FEPasswordHandler(counter, BEServers);
            passwordProcessor = new A1Password.Processor(passwordHandler);

            managementHandler = new FEManagementHandler(counter, BEServers);
            managementProcessor = new A1Management.Processor(managementHandler);
			
			int pport = Integer.parseInt(params.get("-pport"));
            passwordThread = new FEPasswordThread(passwordProcessor, pport);

			int mport = Integer.parseInt(params.get("-mport"));
            managementThread = new FEManagementThread(managementProcessor, mport);

            new Thread(passwordThread).start();
            new Thread(managementThread).start();
			
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

}
