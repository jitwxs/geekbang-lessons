package org.geektimes.projects.user.web.controller;

import org.apache.commons.io.IOUtils;
import org.geektimes.rest.bean.RestResponse;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api")
public class TestPostController implements RestController {

    @POST
    @Path("/testPost")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        final String requestBody = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        return RestResponse.success(requestBody);
    }
}
