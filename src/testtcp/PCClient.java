package testtcp;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class PCClient {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String ans = "y";
		Scanner sc = new Scanner(System.in);
		System.out.println(":: Device Scan ::");
		while (!TCPconnection()) {
			System.out.println(":: Device not found ::");
			do{
				System.out.print("Do you want to continued scan for device again? (y/n) : ");
				ans = sc.nextLine();
				ans = ans.trim().toLowerCase();
			}while(ans.compareToIgnoreCase("n")!=0&&ans.compareToIgnoreCase("y")!=0);
			if(ans.compareToIgnoreCase("n")==0) {
				exitProgram();
				break;
			}
		}
		System.out.println("Device Connected!!");
		System.out.println(":: Start Program ::");
        Scanner scanner = new Scanner(System.in);
        while (true) {
        	System.out.print("Message : ");
            String msg = scanner.nextLine();
            initConnection();
        }
	}
	
	private static boolean TCPconnection(){
		try {
			Process p = Runtime.getRuntime().exec("C:\\Users\\BenzRST\\Desktop\\adb.exe forward tcp:38300 tcp:38300");
			Scanner sc = new Scanner(p.getErrorStream());
			if (sc.hasNext()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static void exitProgram(){
		System.out.println(":: Exit program ::");
		System.exit(0);
	}
	private static void initConnection(){
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket("localhost", 38300);
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			Scanner sc = new Scanner(socket.getInputStream());
			Thread closeSocketOnShutdown = new Thread(){
				public void run() {
					try{
						socket.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			};
			Runtime.getRuntime().addShutdownHook(closeSocketOnShutdown);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
