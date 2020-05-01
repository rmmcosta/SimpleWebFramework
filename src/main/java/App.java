import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class App {
    private final Router router;

    public App(Router router) {
        this.router = router;
        //define which resources should be served according to each requests
        router.addRoute("/", new HelloFactory());
        router.addRoute("/json", new JsonFactory());
        router.addRoute("/person",new PersonFactory());
    }

    public void start(int port, int timeout) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try {
                serverSocket.setSoTimeout(timeout);
                Socket clientSocket = serverSocket.accept();
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                byte[] buffer = new byte[1000];
                int total = input.read(buffer);
                String request = new String(Arrays.copyOfRange(buffer, 0, total)).trim();
                System.out.println("on app, the request received:" + request);
                System.out.println("end of request ---------------");
                //process the request and build a response
                Resource resource;
                try {
                    resource = router.getResource(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    resource = new NoRouteResource();
                }
                output.write(new Session(resource).response().getBytes());
                input.close();
                output.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
