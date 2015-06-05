package ece454750s15a1;

import java.util.concurrent.CopyOnWriteArrayList;
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

	public CopyOnWriteArrayList<ServerNode> BEServers;
	public CopyOnWriteArrayList<ServerNode> FEServers;
	
	private long m_startTime;
	public int numRequestsReceived;
	public int numRequestsCompleted;
	
	private int m_serverIndex;
	private int m_requestCount;
	
	private ServerNode m_serverNode;
    public FEManagementHandler(ServerNode fe) {
		m_startTime = System.currentTimeMillis();
		this.BEServers = new CopyOnWriteArrayList<ServerNode>();
		this.FEServers = new CopyOnWriteArrayList<ServerNode>();
		this.m_serverNode = fe;
		FEServers.add(m_serverNode);
    }

    public PerfCounters getPerfCounters() {
		long curTime = System.currentTimeMillis();
		PerfCounters perfCounter = new PerfCounters((int)(curTime - m_startTime)/1000, numRequestsReceived, numRequestsCompleted);
        return perfCounter;
    }

    public List<String> getGroupMembers() {
        return null;
    }
	
	public void addServerNode(ServerNode node)
	{
		System.out.println("adding...");
		if(node.isBE)
		{
            System.out.println("-- Adding BE:"+node);
			BEServers.add(node);
		}
		else
		{
            System.out.println("-- Adding FE:"+node);
			FEServers.add(node);
		}
	}
	
	public void removeServerNode(ServerNode node)
	{
		System.out.println("removing...");
		if(node.isBE)
		{
            System.out.println("-- Removing BE:"+node);
			BEServers.remove(node);
		}
		else
		{
            System.out.println("-- Removing FE:"+node);
			FEServers.remove(node);
		}
	}
	
	public void setServerList(List<ServerNode> list, boolean isBE)
	{
		if (isBE)
		{
			BEServers = new CopyOnWriteArrayList<ServerNode>(list);
		}
		else
		{
			FEServers = new CopyOnWriteArrayList<ServerNode>(list);
		}
		
	}
	
	public void gossipServerList()
	{
		for(ServerNode sn : FEServers)
		{
			System.out.println("FE # = " + FEServers.size());
			System.out.println("BE # = " + BEServers.size());
			if(sn.equals(m_serverNode))
			{
				continue;
			}
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
		System.out.println("load balancing begin...");
		m_serverIndex = m_serverIndex%BEServers.size();
		try
		{
			while(BEServers.size() > 0)
			{
				ServerNode sn = BEServers.get(m_serverIndex);
				if(m_requestCount < sn.ncores)
				{
					m_requestCount++;
					System.out.println("load balancing end...");
					return sn;
				}
				else
				{
					m_requestCount = 0;
				}
				m_serverIndex = (m_serverIndex + 1)%BEServers.size();
			}
			m_requestCount = 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
