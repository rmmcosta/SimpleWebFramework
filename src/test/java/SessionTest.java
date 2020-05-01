import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SessionTest {
    private static String response;
    @BeforeAll
    static void setUp() {
        Router router = new Router();
        Session session = new Session(new HelloResource());
        String request = "GET / HTTP/1.1\n" +
                "Connection: Upgrade, HTTP2-Settings\n" +
                "Content-Length: 0\n" +
                "Host: localhost:8080\n" +
                "HTTP2-Settings: AAEAAEAAAAIAAAABAAMAAABkAAQBAAAAAAUAAEAA\n" +
                "Upgrade: h2c\n" +
                "User-Agent: Java-http-client/14";
        response = session.response();
    }

    @Test
    void responseHelloWorld() {
        System.out.println(response);
        Pattern pattern = Pattern.compile("Hello World!");
        Matcher matcher = pattern.matcher(response);
        String body="";
        if (matcher.find()) {
            body = matcher.group();
        }
        assertEquals("Hello World!",body);
    }

    @Test
    void responseOK() {
        assertThat(response, Matchers.containsString("HTTP/1.1 200 OK"));
    }
}