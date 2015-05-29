
import org.apache.thrift.TException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class BEPasswordHandler implements A1Password.Iface {

  private BCrypt bcrypt;

  public BEPasswordHandler() {
	BCrypt bcrypt = new BCrypt();
  }
  
  public String hashPassword (String password, short logRounds) throws ServiceUnavailableException {
	return bcrypt.hashpw(password, bcrypt.gensalt(logRounds));
  }

  public boolean checkPassword (String password, String hash) {
	return bcrypt.checkpw(password, hash);
  }
}

