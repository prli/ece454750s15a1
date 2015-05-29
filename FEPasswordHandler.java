
import org.apache.thrift.TException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class FEPasswordHandler implements A1Password.Iface {

  private PerfCounters counter;
  private BCrypt bcrypt;
  
  public FEPasswordHandler(PerfCounters counter) {
	this.counter = counter;
  }
  
  public String hashPassword (String password, short logRounds) throws ServiceUnavailableException {
    counter.numRequestsReceived++;
	String hash = bcrypt.hashpw(password, bcrypt.gensalt(logRounds));
	counter.numRequestsCompleted++;
	return hash;
  }

  public boolean checkPassword (String password, String hash) {
	return bcrypt.checkpw(password, hash);
  }
}

