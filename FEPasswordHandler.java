
import org.apache.thrift.TException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class FEPasswordHandler implements A1Password.Iface {

  private BCrypt bcrypt;

  public FEPasswordHandler() {
  }

  public String hashPassword (String password, short logRounds) throws ServiceUnavailableException {
  }

  public boolean checkPassword (String password, String hash) {
  }
}

