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

public class BEServer {

    public static BEPasswordHandler passwordHandler;
    public static BEManagementHandler managementHandler;

    public static A1Password.Processor passwordProcessor;
    public static A1Management.Processor managementProcessor;

    public static void main(String [] args) {
        
        HashMap params = new HashMap();
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

        try {
            PerfCounters counter = new PerfCounters();

            passwordHandler = new BEPasswordHandler(counter);
            passwordProcessor = new A1Password.Processor(passwordHandler);

            managementHandler = new BEManagementHandler(counter);
            managementProcessor = new A1Management.Processor(managementHandler);

            Runnable psw = new Runnable() {
                public void run() {
                    simple(passwordProcessor);
                }
            };

            Runnable manage = new Runnable() {
                public void run() {
                    simple(managementProcessor);
                }
            };

            new Thread(psw).start();
            new Thread(manage).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(A1Password.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(34950);
            TServer server = new TSimpleServer(
                                               new Args(serverTransport).processor(processor));

            System.out.println("Starting the BE server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void simple(A1Management.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(44950);
            TServer server = new TSimpleServer(
                                               new Args(serverTransport).processor(processor));
            
            System.out.println("Starting the BE server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
