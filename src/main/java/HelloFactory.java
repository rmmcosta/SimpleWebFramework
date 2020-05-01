import javax.validation.constraints.Null;

public class HelloFactory implements ResourceFactory {
    @Override
    public Resource makeResource(@Null int id) {
        return new HelloResource();
    }
}
