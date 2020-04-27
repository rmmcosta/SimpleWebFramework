import javax.lang.model.element.AnnotationValue;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Resource resource;
    public Session(Resource resource) {
        this.resource = resource;
    }

    String response(String request) {
        Map<String, String> pairs = new HashMap<>();
        String[] lines = request.split("\r\n");
        //the line 0 are the headers
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(":");
            pairs.put(parts[0].trim(), parts[1].trim());
            if (lines[i].isEmpty()) {
                break;
            }
        }
        String[] parts = lines[0].split(" ");
        pairs.put("X-Method", parts[0]);
        pairs.put("X-Query", parts[1]);
        pairs.put("X-Protocol", parts[2]);
        //App.Resource appResource = this.resource;
        StringBuilder buffer = new StringBuilder();
        resource.print(new StringBuilderOutput(buffer));
        return buffer.toString();
    }
}
