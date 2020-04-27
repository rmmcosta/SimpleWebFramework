public interface Resource {
    Resource refine(String name, String value);
    void print(Output output);
}
