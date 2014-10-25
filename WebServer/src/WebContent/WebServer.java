package WebContent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class WebServer implements Runnable{
	Socket client = null;
	String getHeader = null;
	BufferedReader br;
	InputStream is;
	OutputStream os;
	PrintWriter out;
	BufferedWriter myWriter;
	
	public WebServer(Socket client){
		this.client = client;
	}

	public void run(){
		try{
			int i = 0;
			while (true) {
				is = client.getInputStream();
			    os = client.getOutputStream();
				br = new BufferedReader(new InputStreamReader(is));
				out = new PrintWriter(os);
				String request = br.readLine();
				System.out.println("Request: "+request);
				
				String httpGet[] = request.split(" ");
				System.out.println("httpGet: "+httpGet[1]);
				if (httpGet[1].contains("header=show")){
					getHeader = request+"<br><br>";
				}else{
					getHeader = "";
				}
				//out.println("HTTP/1.0 200 Ok\n"+ "Content-type: "+getHeader);
				out.print("<HTML><HEAD><TITLE></TITLE></HEAD>"
						+ "<BODY><H2>Information</H2>" + "<P>Request: <PRE>");
				for (int k = 0; k < i - 1; k++)
					out.println(httpGet[k]);
				out.print("</PRE></BODY></HTML>");
				out.flush();
				
				request = request.split("GET /")[1].split(" HTTP")[0];
				System.out.println("\"" + request + "\"");
				File file = new File(request);
				if (file.exists()) {
					System.out.println(file.exists());
					System.out.println("Path = " + file.getCanonicalPath());
					URLConnection url = file.toURI().toURL().openConnection();
					client.getOutputStream().write("HTTP/1.0 200 OK\n".getBytes());
					System.out.write("HTTP/1.0 200 OK\n".getBytes());
					client.getOutputStream().write(("Content-type: " + url.getContentType() + "\n").getBytes());
					System.out.write(("Content-type: " + url.getContentType() + "\n").getBytes());
					client.getOutputStream().write(("Content-length: " + url.getContentLength() + "\n\n").getBytes());
					System.out.write(("Content-length: "+ url.getContentLength() + "\n\n").getBytes());
					FileInputStream isr = new FileInputStream(file);
					while (isr.available() > 0) {
						os.write(isr.read());
						os.flush();
					}
					if (request.contains("..")) {
						System.out.println("Kein richtiger Pfad angegeben - Bad Request");
						final String badrequest = "<html><head><title>400</title></head><body><h1>400 - Bad Request</h1></body></html>";
						final String data = fault(badrequest.length(),"400 Bad Request");

						client.getOutputStream().write(data.getBytes());
						client.getOutputStream().write(badrequest.getBytes());
					}
				}
				// Ausgabe der Fehlermeldung 404
				else {
					if (!(file.exists())) {
						System.out.println("Datei wurde nicht gefunden, schreibe 404 Meldung");
						final String content = "<html><head><title>404</title></head><body><h1>404 - Not found</h1></body></html>";
						final String data = fault(content.length(),"404 Not Found");
						client.getOutputStream().write(data.getBytes());
						client.getOutputStream().write(content.getBytes());
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String fault(final int stringLength, final String httpState) {
		return "HTTP/1.1 " + httpState + "\n" + "Content-Length: "
				+ stringLength + "\n" + "Content-Type: text/html\n"
				+ "Connection: close\n\n";
	}
}