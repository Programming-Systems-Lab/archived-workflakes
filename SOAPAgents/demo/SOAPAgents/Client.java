package SOAPAgents;

import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.ArrayList;
import AddressPackage.ws.Address;
import AddressPackage.ws.PhoneNumber;
import java.rmi.*;
import java.rmi.Naming;
import java.io.*;
import java.lang.String;

class Client{
	SOAPAgentInterface repInt;
	String addressBookURL = new String("http://localhost:8080/axis/services/AddressBook");
	String smtpClientURL = new String("http://vesey.psl.cs.columbia.edu/SMTP_Client/SMTP_Client.asmx");
	Class[] complex = {Address.class, PhoneNumber.class};
	

	public Client(String host) throws RemoteException{
		try{

		repInt = (SOAPAgentInterface)Naming.lookup("rmi://localhost/AddressBookClientService");

		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public String editPhone() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String name = new String("");
		PhoneNumber phone = new PhoneNumber();
		
		try{
			System.out.print("Name: ");
			name = stdin.readLine();
			System.out.print("Area Code: ");
			phone.setAreaCode(Integer.parseInt(stdin.readLine()));
			System.out.print("Number: ");
			phone.setNumber(stdin.readLine());
		}catch(Exception e){
			return new String("Input Error!");
		}

		Object[] params ={name, phone};
		Class[] classes = {String.class, PhoneNumber.class};
		Class output = String.class;	



		Object results = repInt.repair(addressBookURL, "editPhone", params, classes, output, complex); 
			

		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}

		return (String)results;
	}

	public String editEmail() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String name = new String("");
		String email = new String("");
		
		try{
			System.out.print("Name: ");
			name = stdin.readLine();
			System.out.print("Email: ");
			email = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}
		
		Object[] params = {name, email};
		Class[] classes = {String.class, String.class};
		Class output = String.class;	
		
		Object results = repInt.repair(addressBookURL, "editEmail", params, classes, output, complex);

		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}

		return (String)results;
	}

	public String email() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String email = new String("");
		
		try{
			System.out.print("Email: ");
			email = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}
		
		Object[] params = {email};
		Class[] classes = {String.class};
		Class output = String.class;

		Object results = repInt.repair(smtpClientURL, "sendEmail", params, classes, output, complex);


		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}

		return (String)results;
	}

	public String emailAddress() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String name = new String("");
		
		try{
			System.out.print("Name: ");
			name = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}
		

		String add = getAddress(name);
		if(add.equals(name+" not found in Address Book")){
			return add;
		}
		String email = new String("");

		try{
			StringTokenizer token = new StringTokenizer(add, "\n", false);
			for(int i=0; i<4; i++){
				email = token.nextToken();
			}
		}catch(Exception e){
			return new String("Error tokenizing");
		}

		Object[] params = {email};
		Class[] classes = {String.class};
		Class output = String.class;

		Object results = repInt.repair(smtpClientURL, "sendEmail", params, classes, output, complex);

		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}

		return (String)results;
	}

	public String editBody() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String body = new String("");
		
		try{
			System.out.print("Enter new body text: ");
			body = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}
		
		Object[] params = {body};
		Class[] classes = {String.class};
		Class output = String.class;

		Object results = repInt.repair(smtpClientURL, "setBody", 
params, classes, output, complex);
		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}

		return (String)results;
	}

	public String editSubject() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String subject = new String("");
		
		try{
			System.out.print("Enter new subject text: ");
			subject = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}
		
		Object[] params = {subject};
		Class[] classes = {String.class};
		Class output = String.class;

		Object results = repInt.repair(smtpClientURL, "setSubject", params, classes, 
output, complex);
		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}
		return (String)results;
	}

	public String editSenderEmail() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String email = new String("");
		
		try{
			System.out.print("Enter Sender Email: ");
			email = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}


		Object[] params = {email};
		Class[] classes = {String.class};
		Class output = String.class;

		Object results = repInt.repair(smtpClientURL, "setSenderEmail", params, classes, output, complex);
		if(results instanceof Exception){
			return ((Exception)results).getMessage();	
		}
		return (String)results;
	}

	public String remove() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String name = new String("");

		try{
			System.out.print("Name: ");
			name = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}

		Object[] params = {name};
		Class[] classes = {String.class};
		Class output = String.class;

		Object results = repInt.repair(addressBookURL, "removeAddress", params, classes, output, complex);
		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}
		return (String)results;
	}

	public String editAddress() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String name = new String("");
		Address add = new Address();
		
		try{
			System.out.print("Name: ");
			name = stdin.readLine();
			System.out.print("Street Number: ");
			add.setStreetNum(Integer.parseInt(stdin.readLine()));
			System.out.print("Street Name: ");
			add.setStreetName(stdin.readLine());
			System.out.print("City: ");
			add.setCity(stdin.readLine());
			System.out.print("State: ");
			add.setState(stdin.readLine());
			System.out.print("Zip Code: ");
			add.setZip(Integer.parseInt(stdin.readLine()));
		}catch(Exception e){
			return new String("Input Error!");
		}

		return editAddress(name, add);
	}

	public String editAddress(String name, Address add) throws 
RemoteException{
		Object[] params = {name, add};
		Class[] classes = {String.class, Address.class};
		Class output = String.class;

		Object results =repInt.repair(addressBookURL, "editAddress", params, classes, output, complex);
		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}
		return (String)results;
	}
	
	public String editName() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String name = new String("");
		String newName = new String("");

		try{
			System.out.print("Name to Change: ");
			name = stdin.readLine();
			System.out.print("New Name: ");
			newName = stdin.readLine();
		}catch(Exception e){
			return new String("Input Error!");
		}
		
		Object[] params = {name, newName};
		Class[] classes = {String.class, String.class};
		Class output = String.class;

		Object results = repInt.repair(addressBookURL, "editName", params, classes, output, complex);
		if(results instanceof Exception){
			return ((Exception)results).getMessage();
		}
		return (String)results;
	}

	public String getAddress(String name) throws RemoteException{
		Object[] params = {name};
		Class[] classes = {String.class};
		Class output = Address.class;
	
		Object result = repInt.repair(addressBookURL, "getAddressFromName", params, classes, output, complex);

		if(result instanceof Exception){
			return ((Exception)result).getMessage();
		}

		String tmp = new String("");
		if(result==null){
			tmp = new String(name+" not found in Address Book");
		}else{
			tmp = ((Address)result).toString();
		}

		return tmp;

	}

	public String getAllListings() throws RemoteException{
		Class output = Vector.class;

		Object result = repInt.repair(addressBookURL, "getAllListings", null, null, output, complex);


		if(result instanceof Exception){
			return ((Exception)result).getMessage();
		}

		String tmp = new String("");
		if(result == null || ((Vector)result).size() == 0){
			tmp = new String("Address Book is Empty");
		}else{
			for(int i=0; i<((Vector)result).size(); i++){
				tmp+=((String)((Vector)result).get(i))+"\n";
			}
		}

		return tmp;		
	}

	public String getAddress() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String string;

		do{
			System.out.print("Enter the name: ");
			try{
				string = stdin.readLine();
				if(string != null){
					break;
				}else{
					System.out.println("Please re-enter a valid name");
				}
			}catch(Exception e){
				System.out.println(e);
			}
		}while(true);

		return getAddress(string);
	}

	public String addNewEntry() throws RemoteException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String string;
		String name = new String("");
		Address add = new Address();

		try{
			System.out.print("Name: ");
			name = stdin.readLine();
			System.out.print("Street Number: ");
			add.setStreetNum(Integer.parseInt(stdin.readLine()));
			System.out.print("Street Name: ");
			add.setStreetName(stdin.readLine());
			System.out.print("City: ");
			add.setCity(stdin.readLine());
			System.out.print("State: ");
			add.setState(stdin.readLine());
			System.out.print("Zip Code: ");
			add.setZip(Integer.parseInt(stdin.readLine()));
			PhoneNumber phone = new PhoneNumber();
			System.out.print("Area Code: ");
			phone.setAreaCode(Integer.parseInt(stdin.readLine()));
			System.out.print("Number: ");
			phone.setNumber(stdin.readLine());
			add.setPhoneNumber(phone);
			System.out.print("Email: ");
			add.setEmail(stdin.readLine());
		}catch(Exception e){
			return new String("Error with input!");
		}

		Object[] params = {name, add};
		Class[] classes = {String.class, Address.class};
		Class output = String.class;

		Object result = repInt.repair(addressBookURL, "addEntry", params, classes, output, complex);
		if(result instanceof Exception){
			return ((Exception)result).getMessage();
		}
		return (String)result;
	}

	public void displayMenu(){
		System.out.println("\tMain Menu");
		System.out.println("\n");
		System.out.println(" 1. Get Address");
		System.out.println(" 2. View All Listings");
		System.out.println(" 3. Add New Entry");
		System.out.println(" 4. Edit Name");
		System.out.println(" 5. Edit Address");
		System.out.println(" 6. Edit Phone Number");
		System.out.println(" 7. Edit Email Address");
		System.out.println(" 8. Remove Entry");
		System.out.println(" 9. Email A Person");
		System.out.println("10. Email Address");
		System.out.println("11. Edit Email Body");
		System.out.println("12. Edit Email Subject");
		System.out.println("13. Edit Sender's Email");
		System.out.println("14. Get Methods");
		System.out.println("15. Quit");
		System.out.println("\n\n");
		System.out.print("Your Choice: ");
	}

	private String getMethods() throws RemoteException {
		try{
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        	        String string = new String("");
			System.out.println("1: AddressBook methods 2: SMTP_Client methods");
			string = stdin.readLine();
			
			int tmp = Integer.parseInt(string);
			if(tmp == 1) {
				String[] array = repInt.getMethods(addressBookURL);
				String res = new String("");
				for(int i=0; i<array.length-1; i++){
					res+=array[i]+", ";
				}
				res+=array[array.length-1];
				return res;
			}else if(tmp == 2){
				String[] array = repInt.getMethods(smtpClientURL);
				String res = new String("");
				for(int i=0; i<array.length-1; i++){
					res+=array[i]+", ";
				}
				res+=array[array.length-1];
				return res;
			}
		}catch(Exception e){
			return e.getMessage();
		}

		return new String("");
	}

	public void menu() throws Exception{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String string;
		int choice;
		boolean flag = true;
		
		do{
			displayMenu();
			string = stdin.readLine();
			try{
				choice = Integer.parseInt(string);
				if(choice < 1 || choice > 15){
					System.out.println("Invalid Input!");
				}else{
					switch(choice){
					case 1: System.out.println("\n"+getAddress());
						break;
					case 2: System.out.println("\n"+getAllListings());
						break;
					case 3: System.out.println("\n"+addNewEntry());
						break;
					case 4: System.out.println("\n"+editName());
						break;
					case 5: System.out.println("\n"+editAddress());
						break;
					case 6: System.out.println("\n"+editPhone());
						break;
					case 7: System.out.println("\n"+editEmail());
						break;
					case 8: System.out.println("\n"+remove());
						break;
					case 9: System.out.println("\n"+email());
						break;
					case 10: System.out.println("\n"+emailAddress());
						break;
					case 11: System.out.println("\n"+editBody());
						break;
					case 12: System.out.println("\n"+editSubject());
						break;
					case 13: System.out.println("\n"+editSenderEmail());
						break;
					case 14: System.out.println("\n"+getMethods());
						break;
					case 15: flag = false;	
					}
				}
			}catch(Exception e){
				flag = true;
				System.out.println("Invalid Input!");
				e.printStackTrace();
			}
		}while(flag);

		System.out.println("Done!");
			
	}

	public static void main(String argv[]) throws Exception{
		if(argv.length != 1){
			System.out.println("Syntax - Client host");
			System.exit(1);
		}

		Client client = new Client(argv[0]);
		client.menu();
	}
}
