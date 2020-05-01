public class HelloResource implements Resource {
    @Override
    public void print(Output output) {
        output.print("Content-Type", "text/plain");
        output.print("content-length", "12");
        output.print("X-Body","Hello World!");
    }
}
