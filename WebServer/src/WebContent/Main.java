package WebContent;

public class Main {

	public static void main(String[] args) {
		WebServer web = new WebServer();
		new Thread(web).start();
		//test test
	}
}
