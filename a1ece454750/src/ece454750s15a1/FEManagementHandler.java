package ece454750s15a1;

import java.util.ArrayList;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class FEManagementHandler implements A1Management.Iface {

	public ArrayList<ServerNode> BEServers;
	public ArrayList<ServerNode> FEServers;
	
	private long m_startTime;
	public int numRequestsReceived;
	public int numRequestsCompleted;
	
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
		if(isBE)
		{
			BEServers.add(node);
		}
		else
		{
			FEServers.add(node);
		}
	}
	
	public void removeServerNode(ServerNode node, boolean isBE)
	{
		if(isBE)
		{
			BEServers.remove(node);
		}
		else
		{
			FEServers.remove(node);
		}
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
