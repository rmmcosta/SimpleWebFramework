import com.google.gson.JsonObject;

public class PersonResource implements Resource {
    private int id;

    public PersonResource(int id) {
        this.id = id;
    }

    @Override
    public void print(Output output) {
        //do some query to the database with the id
        //but here I will just return a different json
        output.print("content-type", "application/json");
        JsonObject jsonObject;
        String body = "";
        switch (id) {
            case 1:
                jsonObject = new JsonObject();
                jsonObject.addProperty("name", "Ricardo");
                jsonObject.addProperty("age",34);
                body = jsonObject.toString();
                break;
            case 2:
                jsonObject = new JsonObject();
                jsonObject.addProperty("name", "Ana");
                jsonObject.addProperty("age",37);
                body = jsonObject.toString();
                break;
            default:
                body = "person not found";
                break;
        }
        output.print("content-length", String.valueOf(body.length()));
        output.print("X-Body",body);
    }
}
