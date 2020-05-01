import java.util.HashMap;
import java.util.Map;

class Router {
    Map<String, ResourceFactory> resourceMap;

    public Router() {
        resourceMap = new HashMap<>();
    }

    public void addRoute(String route, ResourceFactory factory) {
        resourceMap.put(route, factory);
    }

    public Resource getResource(String request) throws Exception {
        //get the header
        String[] lines = request.split("\r\n");
        /*Map<String, String> pairs = new HashMap<>();
        //the line 0 are the headers
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(":");
            pairs.put(parts[0].trim(), parts[1].trim());
            if (lines[i].isEmpty()) {
                break;
            }
        }*/
        String[] parts = lines[0].split(" ");
        /*pairs.put("X-Method", parts[0]);
        pairs.put("X-Query", parts[1]);
        pairs.put("X-Protocol", parts[2]);*/
        String xQuery = parts[1];
        System.out.println("1st xquery: " + xQuery);
        //if x-query has an id let's split it
        if (resourceMap.containsKey(xQuery)) {
            System.out.println("1st contains");
            return resourceMap.get(xQuery).makeResource(0);
        } else {
            xQuery = xQuery.replace("/", " ").trim();
            System.out.println("2nd xquery: " + xQuery);
            String[] xQueryParts = xQuery.split(" ");
            String route = "/" + xQueryParts[0];
            int id = Integer.parseInt(xQueryParts[1].trim());
            System.out.println("route: " + route + ", id: " + id);
            if (resourceMap.containsKey(route)) {
                return resourceMap.get(route).makeResource(id);
            }
            throw new Exception("Route not defined");
        }
    }
}
