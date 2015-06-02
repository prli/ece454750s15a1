package ece454750s15a1;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

// Generated code
import ece454750s15a1.*;

public class BEManagementHandler implements A1Management.Iface {

    private List<String> groupMembers = Arrays.asList("prli", "p8zhao");
    private PerfCounters counter;
    private ArrayList<ServerNode> BEServers;
    private ArrayList<ServerNode> FEServers;

    public BEManagementHandler(PerfCounters counter) {
        this.counter = counter;
    }

    public PerfCounters getPerfCounters() {
        return counter;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
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
