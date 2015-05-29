import java.util.List;
import java.util.Arrays;

// Generated code
import ece454750s15a1.*;

public class FEManagementHandler implements A1Management.Iface {

  private PerfCounters counter;
  
  public FEManagementHandler(PerfCounters counter) {
	this.counter = counter;
  }
  
  public PerfCounters getPerfCounters() {
	return counter;
  }
  
  public List<String> getGroupMembers() {
	return null;
  }
}

