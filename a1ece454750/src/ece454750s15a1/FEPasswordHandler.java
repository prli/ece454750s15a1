package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import org.apache.thrift.TException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.ArrayList;

// Generated code
import ece454750s15a1.*;

public class FEPasswordHandler implements A1Password.Iface {

    private PerfCounters m_counter;
	private ArrayList<ServerNode> m_BEServers;
    private BCrypt bcrypt;
	
    public FEPasswordHandler(PerfCounters counter, ArrayList<ServerNode> BEServers) {
        this.m_counter = counter;
		this.m_BEServers = BEServers;
    }

    public String hashPassword (String password, short logRounds) throws ServiceUnavailableException {
        //determine addr and port with load balancing
        ServerNode bestBE = loadBalancing();
		String addr = "localhost";
        int port = 34950;
		
        TTransport m_passwordTransport = new TSocket(addr, port);
        try{
            m_passwordTransport.open();

            TProtocol protocol = new TBinaryProtocol(m_passwordTransport);
            A1Password.Client BEServer = new A1Password.Client(protocol);
			m_counter.numRequestsReceived++;
            String hash = BEServer.hashPassword(password, logRounds);
			m_counter.numRequestsCompleted++;
			return hash;
        }catch(TException x){
            x.printStackTrace();
        }finally{
            m_passwordTransport.close();
        }
        return null;
    }

    public boolean checkPassword (String password, String hash) {
        //determine addr and port with load balancing
        ServerNode bestBE = loadBalancing();
		String addr = bestBE.host;
        int port = bestBE.pport;

        TTransport m_passwordTransport = new TSocket(addr, port);
        try{
            m_passwordTransport.open();

            TProtocol protocol = new TBinaryProtocol(m_passwordTransport);
            A1Password.Client BEServer = new A1Password.Client(protocol);
            return BEServer.checkPassword(password, hash);
        }catch(TException x){
            x.printStackTrace();
        }finally{
            m_passwordTransport.close();
        }
        return false;
    }
	
	private ServerNode loadBalancing()
	{
		//arraylist search based on free cores
		ServerNode bestBE = null;
		for(ServerNode s : m_BEServers)
		{
			if(bestBE == null)
			{
				bestBE = s;
			}
			else if(s.ncores - s.usedcores > bestBE.ncores - bestBE.usedcores)
			{
				bestBE = s;
			}
		}
		return bestBE;
	}
}
