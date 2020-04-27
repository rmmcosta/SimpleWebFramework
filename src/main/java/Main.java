import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Blob;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(10000);
            while (true) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
