package WebContent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		
		int port = 80;
		
		try{
			ServerSocket server = new ServerSocket(port);
			while (true) {
			System.out.println("Warte auf Anfrage auf Port: "+ server.getLocalPort());
			Socket client = server.accept();
			Runnable r = new WebServer(client);
			Thread t = new Thread(r);
			t.start();
		}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
