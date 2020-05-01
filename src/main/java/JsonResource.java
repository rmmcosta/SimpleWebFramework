import com.google.gson.JsonObject;
import com.jcabi.http.response.JsonResponse;

public class JsonResource implements Resource {
    @Override
    public void print(Output output) {
        output.print("Content-Type", "application/json");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Ricardo");
        jsonObject.addProperty("age",34);
        output.print("content-length", String.valueOf(jsonObject.toString().length()));
        output.print("X-Body",jsonObject.toString());
    }
}
