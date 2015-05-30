import java.util.ArrayList;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class FEManagementHandler implements A1Management.Iface {

    private PerfCounters counter;
	private ArrayList<ServerNode> BEServers;
	private ArrayList<ServerNode> FEServers;
	
    public FEManagementHandler(PerfCounters counter, ArrayList<ServerNode> BEServers) {
        this.counter = counter;
		this.BEServers = BEServers;
    }

    public PerfCounters getPerfCounters() {
        return counter;
    }

    public List<String> getGroupMembers() {
        return null;
    }
	
	public void addServerNode(String host, int pport, int mport, int ncores)
	{
		ServerNode be = new ServerNode(host, pport, mport, ncores);
		BEServers.add(be);
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
