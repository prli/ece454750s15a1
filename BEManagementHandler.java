import java.util.List;
import java.util.Arrays;

// Generated code
import ece454750s15a1.*;

public class BEManagementHandler implements A1Management.Iface {

  private List<String> groupMembers = Arrays.asList("prli", "p8zhao");
  private PerfCounters counter;

  public BEManagementHandler(PerfCounters counter) {
    this.counter = counter;
  }
  
  public PerfCounters getPerfCounters() {
    return counter;
  }
  
  public List<String> getGroupMembers() {
	return groupMembers;
  }
}

