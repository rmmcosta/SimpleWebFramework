import javax.validation.constraints.Null;

public class JsonFactory implements ResourceFactory {
    @Override
    public Resource makeResource(@Null int id) {
        return new JsonResource();
    }
}
