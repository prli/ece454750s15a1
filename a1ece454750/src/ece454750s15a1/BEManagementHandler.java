package ece454750s15a1;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

// Generated code
import ece454750s15a1.*;

public class BEManagementHandler implements A1Management.Iface {

    private List<String> groupMembers = Arrays.asList("prli", "p8zhao");
	private long m_startTime;
	
	public int numRequestsReceived;
	public int numRequestsCompleted;
	
    public BEManagementHandler() {
        m_startTime = System.currentTimeMillis();
    }

    public PerfCounters getPerfCounters() {
        long curTime = System.currentTimeMillis();
		PerfCounters perfCounter = new PerfCounters((int)(curTime - m_startTime)/1000, numRequestsReceived, numRequestsCompleted);
        return perfCounter;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }
    
    public void addServerNode(ServerNode node, boolean isBE)
    {
		return;
    }
	
	public void removeServerNode(ServerNode node, boolean isBE)
    {
        return;
    }
    
    public List<ServerNode> getAllFEServerNodes()
    {
        return null;
    }
    
    public List<ServerNode> getAllBEServerNodes()
    {
        return null;
    }
}
