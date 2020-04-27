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
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClientTest {
    public static void main(String[] args) {
        HttpClientTest httpClientTest = new HttpClientTest();
        try {
            String uri = "http://localhost:8080";
            System.out.println("get");
            httpClientTest.get(uri);
            System.out.println("apache get");
            httpClientTest.apacheGet(uri);
            System.out.println("jcabi get");
            httpClientTest.jcabiGet(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
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
