import javax.lang.model.element.AnnotationValue;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Resource resource;
    public Session(Resource resource) {
        this.resource = resource;
    }

    public String response() {
        StringBuilder buffer = new StringBuilder();
        resource.print(new StringBuilderOutput(buffer));
        return buffer.toString();
    }
}
