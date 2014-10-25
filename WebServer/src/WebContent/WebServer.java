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
	
	public WebServer(Socket client){
		this.client = client;
	}

	public void run(){
		try{
			String inputLine;
			int i = 0;
			while (true) {
				OutputStream os = client.getOutputStream();
				InputStream is = client.getInputStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
//				BufferedWriter myWriter = new BufferedWriter(new FileWriter(
//						"log.txt", true));
				String request = br.readLine();
//				PrintWriter out = new PrintWriter(os);
				
				// Time time = new Time(System.currentTimeMillis());
				// Date date = new Date(System.currentTimeMillis());
				// String browser = " ";
				// myWriter.write(date.toString() + " " + time.toString() +
				// request);
				// String insert = " ";
				//
				// do {
				// insert = br.readLine(); // lesen der Zeilen im Buffer
				// if (insert.startsWith("User-Agent")) {
				// browser = insert.substring(12);
				// myWriter.write("\n " + insert);
				// System.out.println(browser);
				// }
				// } while (!insert.equals("") && (!(insert == null)));
				// myWriter.write("\n");
				// myWriter.close();

				request = request.split("GET /")[1].split(" HTTP")[0];
				System.out.println("\"" + request + "\"");
				File file = new File(request);
				if (file.exists()) {
					System.out.println(file.exists());
					System.out.println("Path = " + file.getCanonicalPath());
					URLConnection url = file.toURI().toURL().openConnection();
					client.getOutputStream().write("HTTP/1.0 200 OK\n".getBytes());
					System.out.write("HTTP/1.0 200 OK\n".getBytes());
					client.getOutputStream().write(("Content-type: " + url.getContentType() + "\n")
							.getBytes());
					System.out
							.write(("Content-type: " + url.getContentType() + "\n")
									.getBytes());
					client.getOutputStream().write(("Content-length: " + url.getContentLength() + "\n\n")
							.getBytes());
					System.out.write(("Content-length: "
							+ url.getContentLength() + "\n\n").getBytes());
					FileInputStream isr = new FileInputStream(file);
					while (isr.available() > 0) {
						os.write(isr.read());
						os.flush();
					}
					/*
					 * if (request.equals("/")) request =
					 * "/testpage/index.html";
					 * System.out.println("Es wurde die Datei " + request +
					 * " angefragt.");
					 */

					// String htdocs = new String("http://localhost:30000/");
					// String temp;
					// String Path;
					// boolean quit = false;
					// temp = (new File(htdocs + request)).getCanonicalPath();
					// String Path = (new File(temp)).getCanonicalPath();
					// Bad request
					if (request.contains("..")) {
						System.out.println("Kein richtiger Pfad angegeben - Bad Request");
						final String badrequest = "<html><head><title>400</title></head><body><h1>400 - Bad Request</h1></body></html>";
						final String data = fault(badrequest.length(),
								"400 Bad Request");

						client.getOutputStream().write(data.getBytes());
						client.getOutputStream().write(badrequest.getBytes());

					}
				}
				// Ausgabe der Fehlermeldung 404
				else {
					if (!(file.exists())) {
						System.out
								.println("Datei wurde nicht gefunden, schreibe 404 Meldung");
						final String content = "<html><head><title>404</title></head><body><h1>404 - Not found</h1></body></html>";
						final String data = fault(content.length(),
								"404 Not Found");
						client.getOutputStream().write(data.getBytes());
						client.getOutputStream().write(content.getBytes());
					}

				}

			}

		} catch (IOException e) {
			System.out
					.println("Datei wurde nicht gefunden, schreibe 404 Meldung");
			final String content = "<html><head><title>404</title></head><body><h1>404 - Not found</h1></body></html>";
			final String data = fault(content.length(), "404 Not Found");
			try {
				client.getOutputStream().write(data.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				client.getOutputStream().write(content.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Exception" + e);
		}
	}

	public static String fault(final int stringLength, final String httpState) {
		return "HTTP/1.1 " + httpState + "\n" + "Content-Length: "
				+ stringLength + "\n" + "Content-Type: text/html\n"
				+ "Connection: close\n\n";
	}

	public static void displayHeader(PrintWriter out){
		int i = 0;
		String line[] = new String[10];
//		while (!(line[i++] = request).equals(""));
		out.println("HTTP\1.0 200 OK");
		out.println("Content-type: text/html");
		out.println("");
		out.print("<HTML><HEAD><TITLE></TITLE></HEAD>"
				+ "<BODY><H2>Information</H2>" + "<P>Request: <PRE>");
		for (int k = 0; k < i - 1; k++)
			out.println(line[k]);
		out.print("</PRE></BODY></HTML>");
		out.flush();
	}
}