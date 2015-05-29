import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

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
        //determine addr and port with load balancing
        String addr = "localhost";
        int port = 34950;

        TTransport m_passwordTransport = new TSocket(addr, port);
        try{
            m_passwordTransport.open();

            TProtocol protocol = new TBinaryProtocol(m_passwordTransport);
            A1Password.Client BEServer = new A1Password.Client(protocol);
            return BEServer.hashPassword(password, logRounds);
        }catch(TException x)
        {
            x.printStackTrace();
        }finally
        {
            m_passwordTransport.close();
        }
        return null;
    }

    public boolean checkPassword (String password, String hash) {
        //determine addr and port with load balancing
        String addr = "localhost";
        int port = 34950;

        TTransport m_passwordTransport = new TSocket(addr, port);
        try{
            m_passwordTransport.open();

            TProtocol protocol = new TBinaryProtocol(m_passwordTransport);
            A1Password.Client BEServer = new A1Password.Client(protocol);
            return BEServer.checkPassword(password, hash);
        }catch(TException x)
        {
            x.printStackTrace();
        }finally
        {
            m_passwordTransport.close();
        }
        return false;
    }
}
