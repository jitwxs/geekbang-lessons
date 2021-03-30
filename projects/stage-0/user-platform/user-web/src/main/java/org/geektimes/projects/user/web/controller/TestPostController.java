package org.geektimes.projects.user.web.controller;

import org.geektimes.rest.bean.RestResponse;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api")
public class TestPostController implements RestController {

    @GET
    @Path("/testPost")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return RestResponse.success("Hello Post!");
    }
}
