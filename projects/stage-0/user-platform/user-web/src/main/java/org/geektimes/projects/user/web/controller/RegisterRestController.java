package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.RestResponse;
import org.geektimes.web.mvc.controller.RestController;
import org.geektimes.web.mvc.id.IdGenerator;

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
public class RegisterRestController implements RestController {
    @Resource(name = "bean/UserService")
    private UserService userService;

    @POST
    @Path("/register")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        final String username = request.getParameter("inputUsername");
        final String password = request.getParameter("inputPassword");
        final String tel = request.getParameter("inputTel");
        final String email = request.getParameter("inputEmail");

        User user = new User();
        user.setId(IdGenerator.nextId());
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(tel);

        return userService.register(user);
    }
}
