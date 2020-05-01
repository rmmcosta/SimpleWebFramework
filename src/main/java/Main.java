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
        App app = new App(new Router());
        try {
            app.start(portNumber, 20000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
