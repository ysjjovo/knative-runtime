package runtime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public class UnifyResource {
    @GET
    @Path("/")
    @Produces("text/plain")
    public String run() {
        return "FaaS Platform!";
    }
}