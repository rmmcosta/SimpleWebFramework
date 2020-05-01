import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {
    private static Router router;

    @BeforeEach
    void setUp() {
        router = new Router();
    }

    @Test
    void getHelloResource() throws Exception {
        String request = "GET / HTTP/1.1\n";
        router.addRoute("/", new HelloFactory());
        Resource resource = router.getResource(request);
        assertTrue(resource instanceof HelloResource);
    }

    @Test
    void getJsonResource() throws Exception {
        String request = "GET /json HTTP/1.1\n";
        router.addRoute("/json", new JsonFactory());
        Resource resource = router.getResource(request);
        assertTrue(resource instanceof JsonResource);
    }

    @Test
    void getPersonResource() throws Exception {
        String request = "GET /person/1 HTTP/1.1";
        router.addRoute("/person", new PersonFactory());
        Resource resource = router.getResource(request);
        assertTrue(resource instanceof PersonResource);
        Session session = new Session(resource);
        System.out.println(session.response());
    }
}