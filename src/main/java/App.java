import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class App {
    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        Socket clientSocket = serverSocket.accept();
        InputStream input = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        byte[] buffer = new byte[1000];
        int total = input.read(buffer);
        String request = new String(Arrays.copyOfRange(buffer, 0, total));
        System.out.println(request);
        String response = "HTTP/1.1 200 OK\r\n\r\nHello World!";
        output.write(response.getBytes());
        clientSocket.close();
    }
}
