package ece454750s15a1;

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
	public static class BEPasswordThread implements Runnable {
		private A1Password.Processor processor;
		private int pport;
		public BEPasswordThread(A1Password.Processor p, int port) {
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

            System.out.println("Starting the BE password server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static class BEManagementThread implements Runnable {
		private A1Management.Processor processor;
		private int mport;
		public BEManagementThread(A1Management.Processor p, int port) {
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
            
            System.out.println("Starting the BE management server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public static BEPasswordHandler passwordHandler;
    public static BEManagementHandler managementHandler;

    public static A1Password.Processor passwordProcessor;
    public static A1Management.Processor managementProcessor;

	public static BEPasswordThread passwordThread;
	public static BEManagementThread managementThread;
	
    public static void main(String [] args) {

/*         String [] argLiteral = {"-host", "localhost",
            "-pport", "34950",
            "-mport", "44950",
            "-ncores","2",
            "-seeds","localhost:24950"
        };
        
        args = argLiteral; */
        
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

        try {
			String addr = params.get("-host");
			int pport = Integer.parseInt(params.get("-pport"));
			int mport = Integer.parseInt(params.get("-mport"));
			int ncores = Integer.parseInt(params.get("-ncores"));
			
			ServerNode node = new ServerNode(addr, pport, mport, ncores, true, false);
			
			managementHandler = new BEManagementHandler();
            managementProcessor = new A1Management.Processor(managementHandler);
			
            passwordHandler = new BEPasswordHandler(managementHandler);
            passwordProcessor = new A1Password.Processor(passwordHandler);

            passwordThread = new BEPasswordThread(passwordProcessor, pport);
			
            managementThread = new BEManagementThread(managementProcessor, mport);

            new Thread(passwordThread).start();
            new Thread(managementThread).start();

			joinCluster(node, seedHosts, seedPorts);
			
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
	
	public static void joinCluster(ServerNode node, ArrayList<String> seedHosts, ArrayList<Integer> seedPorts) throws TException
	{
		int seedIndex = 0;
		while(true){
			TTransport transport = new TSocket(seedHosts.get(seedIndex), seedPorts.get(seedIndex));
			try
			{
				transport.open();

				TProtocol protocol = new  TBinaryProtocol(transport);
				A1Management.Client client = new A1Management.Client(protocol);
				
				client.addServerNode(node);
				return;
			}
			catch(TException e)
			{
				seedIndex = (seedIndex + 1)%seedHosts.size();
			}
			finally
			{
				transport.close();
				try
				{
					Thread.sleep(100); //try again after 100ms
				}
				catch(InterruptedException e)
				{
				
				}
			}
		}
	}
}
