import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @org.junit.jupiter.api.Test
    void start() throws InterruptedException {
        int port = 8080;
        final ArrayList<Integer> threadsCompleted = new ArrayList<Integer>();
        Runnable serverThread = new Runnable() {
            @Override
            public void run() {
                App app = new App();
                try {
                    System.out.println("app starting on port:" + port);
                    app.start(port);
                    assertTrue(true);
                    System.out.println("app running on port:" + port);
                } catch (IOException e) {
                    assertTrue(false);
                    System.out.println(e.getMessage());
                    throw new IllegalStateException();
                }
                threadsCompleted.add(1);
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("starting client thread on port:" + port);
                    String status = apacheGet("http://localhost:" + port);
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
        };

        Thread t1 = new Thread(serverThread);
        Thread t2 = new Thread(runnable2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Threads completed: " + threadsCompleted);
        assertEquals(2, threadsCompleted.size());
    }

    public String get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        return String.valueOf(response.statusCode());
    }

    public String apacheGet(String uri) {
        AtomicInteger theStatus = new AtomicInteger();
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet(uri);

            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

            // Create a custom response handler
            final HttpClientResponseHandler<String> responseHandler = response -> {
                final int status = response.getCode();
                theStatus.set(status);
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    final HttpEntity entity = response.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            final String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return theStatus.toString();
    }

    public String jcabiGet(String uri) {
        int status;
        Request request = new JdkRequest(uri);
        Response response = null;
        try {
            response = request.fetch();
        } catch (IOException e) {
            return "400";
        }
        status = response.status();
        String body = response.body();
        System.out.println(body);
        return String.valueOf(status);
    }
}