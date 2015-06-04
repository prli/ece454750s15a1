package ece454750s15a1;

import java.util.*;
import java.util.ArrayList;
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
            "-seeds","localhost:24950"
        };
        
        args = argLiteral;
        
        HashMap<String, String> params = new HashMap<String, String>();
        ArrayList<String>  seedHosts = new ArrayList<String>();
        ArrayList<Integer> seedPorts = new ArrayList<Integer>();

        for(int i = 0 ; i < args.length ; i+=2) {
            params.put(args[i], args[i+1]);
        }
        String seedString = (String)params.get("-seeds");
        String[] seedStrings = seedString.split(",");
        
        for(int i = 0 ; i < seedStrings.length ; i++) {
            String[] seed = seedStrings[i].split(":");
            seedHosts.add(seed[0]);
            seedPorts.add(Integer.parseInt(seed[1]));
        }
        params.remove("-seeds");
        
        System.out.println("params : "+params);
        System.out.println("seedHosts : "+seedHosts);
        System.out.println("seedPorts : "+seedPorts);
        System.out.println("is a seed : "+FEServer.isSeedNode(params,seedHosts,seedPorts));
        
        // Output Example
        // params : {-ncores=2, -mport=9123, -host=ecelinux1, -pport=8123}
        // seedHosts : [ecelinux1, ecelinux2, ecelinux3]
        // seedPorts : [10123, 10123, 10123]

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
    
    // If the host/port matches list of seedHosts and seedPorts at the same index
    public static boolean isSeedNode(HashMap params, ArrayList<String> seedHosts, ArrayList<Integer> seedPorts) {
        
        String host = (String)params.get("-host");
        Integer port = Integer.parseInt((String)params.get("-mport"));
        
        for(int i = 0; i<seedPorts.size();i++) {
            if(host.equals(seedHosts.get(i)) && port.equals(seedPorts.get(i))) {
                return true;
            }
        }
        return false;
    }
}

