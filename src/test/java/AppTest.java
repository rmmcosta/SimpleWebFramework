import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    ArrayList<Integer> threadsCompleted;

    @BeforeEach
    void setUp() {
        threadsCompleted = new ArrayList<>();
    }
    class ServerThread implements Runnable {
        private final int port;

        ServerThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            App app = new App(new Router());
            try {
                System.out.println("app starting on port:" + port);
                app.start(port, 1000);
                assertTrue(true);
                System.out.println("app running on port:" + port);
            } catch (IOException e) {
                assertTrue(false);
                System.out.println(e.getMessage());
                throw new IllegalStateException();
            }
            threadsCompleted.add(1);
        }
    }

    class ClientThread implements Runnable {
        private final int port;
        private final String xQuery;

        ClientThread(int port, String xQuery) {
            this.port = port;
            this.xQuery = xQuery;
        }

        @Override
        public void run() {
            try {
                System.out.println("starting client thread on port:" + port);
                String status = new MyHttpClient("http://localhost:" + port + xQuery).get();
                assertEquals("200", status);
                System.out.println("status clientThread:" + status);
                System.out.println("client thread running on port:" + port);
            } catch (Exception e) {
                assertTrue(false);
                System.out.println(e.getMessage());
                throw new IllegalStateException();
            }
            threadsCompleted.add(2);
        }
    }

    @Test
    void getHello() throws InterruptedException {
        Thread t1 = new Thread(new ServerThread(8080));
        Thread t2 = new Thread(new ClientThread(8080, ""));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Threads completed: " + threadsCompleted);
        assertEquals(2, threadsCompleted.size());
    }

    @Test
    void getJson() throws InterruptedException {
        Thread t1 = new Thread(new ServerThread(8081));
        Thread t2 = new Thread(new ClientThread(8081, "/json"));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Threads completed: " + threadsCompleted);
        assertEquals(2, threadsCompleted.size());
    }

    @Test
    void getPerson() throws InterruptedException {
        Thread t1 = new Thread(new ServerThread(8082));
        Thread t2 = new Thread(new ClientThread(8082, "/person/2"));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Threads completed: " + threadsCompleted);
        assertEquals(2, threadsCompleted.size());
    }

    @AfterEach
    void cleanUp() {
        threadsCompleted.clear();
    }
}