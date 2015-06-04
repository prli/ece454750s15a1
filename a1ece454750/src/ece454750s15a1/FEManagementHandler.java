package ece454750s15a1;

import java.util.ArrayList;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class FEManagementHandler implements A1Management.Iface {

    private PerfCounters counter;
	private ArrayList<ServerNode> BEServers;
	private ArrayList<ServerNode> FEServers;
	
    public FEManagementHandler(PerfCounters counter, ArrayList<ServerNode> BEServers, ArrayList<ServerNode> FEServers) {
        this.counter = counter;
		this.BEServers = BEServers;
		this.FEServers = FEServers;
    }

    public PerfCounters getPerfCounters() {
        return counter;
    }

    public List<String> getGroupMembers() {
        return null;
    }
	
	public void addServerNode(String host, int pport, int mport, int ncores, boolean isBE)
	{
		ServerNode be = new ServerNode(host, pport, mport, ncores);
		if(isBE)
		{
			BEServers.add(be);
		}
		else
		{
			FEServers.add(be);
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
