package ece454750s15a1;

import java.util.*;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

// Generated code
import ece454750s15a1.*;

public class BEServer {
    public static class BEPasswordThread implements Runnable {
        private int cores;
        private int pport;
        private String host;
        private A1Password.Processor processor;
        
        public BEPasswordThread(String host, int pport, int cores, A1Password.Processor processor)  {
            this.host = host;
            this.pport = pport;
            this.cores = cores;
            this.processor = processor;
        }
        
        public void run() {
            BEPasswordBlock(this.host, this.pport, this.cores, this.processor);
        }
    }
    public static class BEManagementThread implements Runnable {
        private int cores;
        private int mport;
        private String host;
        private A1Management.Processor processor;
        
        public BEManagementThread(String host, int mport, int cores, A1Management.Processor processor) {
            this.host = host;
            this.mport = mport;
            this.cores = cores;
            this.processor = processor;
        }
        
        public void run() {
            BEManagementBlock(this.host, this.mport, this.cores, this.processor);
        }
    }
    public static void BEPasswordBlock(String host, int pport, int cores, A1Password.Processor processor) {
        try {
            TNonblockingServerTransport trans = new TNonblockingServerSocket(pport);
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(trans);
            args.transportFactory(new TFramedTransport.Factory());
            args.protocolFactory(new TBinaryProtocol.Factory());
            args.processor(processor);
            args.selectorThreads(cores*2);
            args.workerThreads(cores*16);
            TServer server = new TThreadedSelectorServer(args);
            
            System.out.println("-- BE Password - Host:"+host+" Port:"+pport);
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void BEManagementBlock(String host, int mport, int cores, A1Management.Processor processor) {
        try {
            TNonblockingServerTransport trans = new TNonblockingServerSocket(mport);
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(trans);
            args.transportFactory(new TFramedTransport.Factory());
            args.protocolFactory(new TBinaryProtocol.Factory());
            args.processor(processor);
            args.selectorThreads(cores*2);
            args.workerThreads(cores*16);
            TServer server = new TThreadedSelectorServer(args);
            
            System.out.println("-- BE Manageme - Host:"+host+" Port:"+mport);
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
        
        // Output Example
        // params : {-ncores=2, -mport=9123, -host=ecelinux1, -pport=8123}
        // seedHosts : [ecelinux1, ecelinux2, ecelinux3]
        // seedPorts : [10123, 10123, 10123]
        
        try {
            String host = params.get("-host");
            int pport = Integer.parseInt(params.get("-pport"));
            int mport = Integer.parseInt(params.get("-mport"));
            int ncores = Integer.parseInt(params.get("-ncores"));
            
            ServerNode node = new ServerNode(host, pport, mport, ncores, true, false);
            
            managementHandler = new BEManagementHandler();
            managementProcessor = new A1Management.Processor(managementHandler);
            
            passwordHandler = new BEPasswordHandler(managementHandler);
            passwordProcessor = new A1Password.Processor(passwordHandler);
            
            passwordThread = new BEPasswordThread(host, pport, ncores, passwordProcessor);
            managementThread = new BEManagementThread(host, mport, ncores, managementProcessor);
            
            new Thread(passwordThread).start();
            new Thread(managementThread).start();
            
            for(int i=0;i<seedPorts.size();i++) {
                joinCluster(node, seedHosts.get(i), seedPorts.get(i));
            }
            
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
    
    public static void joinCluster(ServerNode node, String seedHost, int seedPort) throws TException
    {
        TTransport transport = new TFramedTransport(new TSocket(seedHost, seedPort));
        transport.open();
        
        TProtocol protocol = new TBinaryProtocol(transport);
        A1Management.Client client = new A1Management.Client(protocol);

        try {
            client.addServerNode(node);
        } catch (Exception x) {
            x.printStackTrace();
        }
        transport.close();
    }
}
