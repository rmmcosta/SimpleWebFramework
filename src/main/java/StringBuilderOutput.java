public class StringBuilderOutput implements Output {
    private final StringBuilder buffer;

    public StringBuilderOutput(StringBuilder buffer) {
        this.buffer = buffer;
    }

    @Override
    public void print(String name, String value) {
        if (buffer.length() == 0) {
            buffer.append("HTTP/1.1 200 OK\r\n");
        }
        if (name.equals("X-Body")) {
            buffer.append("\r\n").append(value);
        } else {
            buffer.append(name).append(": ").append(value).append("\r\n");
        }
    }
}
