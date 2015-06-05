package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;

// Generated code
import ece454750s15a1.*;

public class FEPasswordHandler implements A1Password.Iface {

    private FEManagementHandler m_FEManagementHandler;
	
    public FEPasswordHandler(FEManagementHandler managementHandler) {
        this.m_FEManagementHandler = managementHandler;
    }

    public String hashPassword (String password, short logRounds) throws ServiceUnavailableException {
		while(m_FEManagementHandler.BEServers.size() > 0)
		{
			//determine addr and port with load balancing
			ServerNode bestBE = m_FEManagementHandler.loadBalancing();
			if(bestBE == null)
			{
				throw new ServiceUnavailableException("no service");
			}
			String addr = bestBE.host;
			int port = bestBE.pport;
			
			TTransport m_passwordTransport = new TSocket(addr, port);
			try{
				m_passwordTransport.open();

				TProtocol protocol = new TBinaryProtocol(m_passwordTransport);
				A1Password.Client BEServer = new A1Password.Client(protocol);
				m_FEManagementHandler.numRequestsReceived++;
				String hash = BEServer.hashPassword(password, logRounds);
				m_FEManagementHandler.numRequestsCompleted++;
				return hash;
			}catch(TException x){
				System.out.println("Cant hash, server is down: " + bestBE);
				m_FEManagementHandler.BEServers.remove(bestBE);
			}finally{
				m_passwordTransport.close();
			}
		}
    }

    public boolean checkPassword (String password, String hash) {
		while(m_FEManagementHandler.BEServers.size() > 0)
		{
			//determine addr and port with load balancing
			ServerNode bestBE = m_FEManagementHandler.loadBalancing();
			String addr = bestBE.host;
			int port = bestBE.pport;

			TTransport m_passwordTransport = new TSocket(addr, port);
			try{
				m_passwordTransport.open();

				TProtocol protocol = new TBinaryProtocol(m_passwordTransport);
				A1Password.Client BEServer = new A1Password.Client(protocol);
				m_FEManagementHandler.numRequestsReceived++;
				boolean checked = BEServer.checkPassword(password, hash);
				m_FEManagementHandler.numRequestsCompleted++;
				return checked;
			}catch(TException x){
				System.out.println("Cant check, server is down: " + bestBE);
				m_FEManagementHandler.BEServers.remove(bestBE);
			}finally{
				m_passwordTransport.close();
			}
		}
		return false;
    }
	
	
}
