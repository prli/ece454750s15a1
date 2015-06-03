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

    public static FEPasswordHandler passwordHandler;
    public static FEManagementHandler managementHandler;

    public static A1Password.Processor passwordProcessor;
    public static A1Management.Processor managementProcessor;

    public static void main(String [] args) {

        String [] argLiteral = {"-host", "ecelinux1",
            "-pport", "8123",
            "-mport", "9123",
            "-ncores","2",
            "-seeds","ecelinux1:10123,ecelinux2:10123,ecelinux3:10123"
        };
        
        args = argLiteral;
        
        HashMap params = new HashMap();
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