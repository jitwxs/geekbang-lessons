package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.RestResponse;
import org.geektimes.web.mvc.controller.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author jitwxs
 * @date 2021年03月06日 16:56
 */
@Path("/api")
public class QueryUserByIdRestController implements RestController {
    @Resource(name = "bean/UserService")
    private UserService userService;

    @POST
    @Path("/user")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        final String id = request.getParameter("id");

        try {
            final User user = userService.queryUserById(Long.parseLong(id));

            if(user == null) {
                return RestResponse.failure(-1, "User Not Exist");
            } else {
                return RestResponse.success(user.toString(), "home.jsp");
            }
        } catch (Exception e) {
            return RestResponse.failure(-1, e.getMessage());
        }
    }
}
