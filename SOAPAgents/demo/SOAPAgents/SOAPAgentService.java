package SOAPAgents;
import java.rmi.Naming;

/**
* This class is used to connect to the Client Web Service via RMI.
* The bind is: rmi://localhost:1099/AddressBookClientService
* @author Serafina Sumargo
*/

public class SOAPAgentService {
	public SOAPAgentService() {
		try{
			SOAPAgentAPI rep = new SOAPAgentAPI();
			
Naming.rebind("rmi://localhost:1099/AddressBookClientService", rep);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	public static void main(String argv[]){
		new SOAPAgentService();
	}
}
