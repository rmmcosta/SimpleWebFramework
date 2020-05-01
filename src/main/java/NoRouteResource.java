public class NoRouteResource implements Resource {
    @Override
    public void print(Output output) {
        output.print("Content-Type", "text/plain");
        output.print("content-length", "16");
        output.print("X-Body","Route not found!");
    }
}
