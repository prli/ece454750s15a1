import java.util.List;
import java.util.Arrays;

// Generated code
import ece454750s15a1.*;

public class BEManagementHandler implements A1Management.Iface {

  private List<String> groupMembers = Arrays.asList("prli", "p8zhao");
  private PerfCounters perfCounters;

  public BEManagementHandler() {
    perfCounters = new PerfCounters();
  }
  
  public PerfCounters getPerfCounters() {
    return perfCounters;
  }
  
  public List<String> getGroupMembers() {
	return groupMembers;
  }
}

