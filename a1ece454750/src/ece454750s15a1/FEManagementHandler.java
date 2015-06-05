package ece454750s15a1;

import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

// Generated code
import ece454750s15a1.*;

public class FEManagementHandler implements A1Management.Iface {

	public ArrayList<ServerNode> BEServers;
	public ArrayList<ServerNode> FEServers;
	
	private long m_startTime;
	public int numRequestsReceived;
	public int numRequestsCompleted;
	
	private int m_serverIndex;
	private int m_requestCount;
	
    public FEManagementHandler() {
		m_startTime = System.currentTimeMillis();
		this.BEServers = new ArrayList<ServerNode>();
		this.FEServers = new ArrayList<ServerNode>();
    }

    public PerfCounters getPerfCounters() {
		long curTime = System.currentTimeMillis();
		PerfCounters perfCounter = new PerfCounters((int)(curTime - m_startTime)/1000, numRequestsReceived, numRequestsCompleted);
        return perfCounter;
    }

    public List<String> getGroupMembers() {
        return null;
    }
	
	public void addServerNode(ServerNode node, boolean isBE)
	{
		System.out.println("adding...");
		if(isBE)
		{
			BEServers.add(node);
		}
		else
		{
			FEServers.add(node);
		}
		gossipServerList();
	}
	
	public void removeServerNode(ServerNode node, boolean isBE)
	{
		System.out.println("removing...");
		if(isBE)
		{
			BEServers.remove(node);
		}
		else
		{
			FEServers.remove(node);
		}
		gossipServerList();
	}
	
	public void setServerList(List<ServerNode> list, boolean isBE)
	{
		if (isBE)
		{
			BEServers = new ArrayList<ServerNode>(list);
		}
		else
		{
			FEServers = new ArrayList<ServerNode>(list);
		}
		
	}
	
	public void gossipServerList()
	{
		for(ServerNode sn : FEServers)
		{
			TTransport transport = new TSocket(sn.host, sn.mport);
			try{
				transport.open();
				TProtocol protocol = new TBinaryProtocol(transport);
				A1Management.Client client = new A1Management.Client(protocol);
				System.out.println(sn);
				client.setServerList(BEServers, true);
				client.setServerList(FEServers, false);
			}catch(TException x){
				System.out.println("gossiping failed...");
			}finally{
				transport.close();
			}
		}
	}
	
	public ServerNode loadBalancing()
	{
		while(BEServers.size() > 0)
		{
			ServerNode sn = BEServers.get(m_serverIndex);
			if(m_requestCount < sn.ncores)
			{
				m_requestCount++;
				return sn;
			}
			else
			{
				m_requestCount = 0;
			}
			m_serverIndex = (m_serverIndex + 1)%BEServers.size();
		}
		m_requestCount = 0;
		return null;
	}
	
	public List<ServerNode> getAllFEServerNodes()
	{
		return FEServers;
	}
	
	public List<ServerNode> getAllBEServerNodes()
	{
		return BEServers;
	}

}
