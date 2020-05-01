public class PersonFactory implements ResourceFactory {
    @Override
    public Resource makeResource(int id) {
        return new PersonResource(id);
    }
}
