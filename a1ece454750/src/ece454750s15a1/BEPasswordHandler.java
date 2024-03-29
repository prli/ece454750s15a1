package ece454750s15a1;

import org.apache.thrift.TException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

// Generated code
import ece454750s15a1.*;

public class BEPasswordHandler implements A1Password.Iface {

    private BEManagementHandler m_BEManagementHandler;
    private BCrypt bcrypt;

    public BEPasswordHandler(BEManagementHandler managementHandler) {
        BCrypt bcrypt = new BCrypt();
        this.m_BEManagementHandler = managementHandler;
    }

    public String hashPassword (String password, short logRounds) throws ServiceUnavailableException {
        try{
			System.out.println("BE hashing begin...");
			m_BEManagementHandler.numRequestsReceived++;
			String hash = bcrypt.hashpw(password, bcrypt.gensalt(logRounds));
			m_BEManagementHandler.numRequestsCompleted++;
			System.out.println("BE hashing end...");
			return hash;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		throw new ServiceUnavailableException("BE service down");
    }

    public boolean checkPassword (String password, String hash) {
		if(hash == null)
		{
			return false;
		}
		try
		{
			System.out.println("BE checking begin...");
			m_BEManagementHandler.numRequestsReceived++;
			boolean checked = bcrypt.checkpw(password, hash);
			m_BEManagementHandler.numRequestsCompleted++;
			System.out.println("BE checking end...");
			return checked;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
    }
}
