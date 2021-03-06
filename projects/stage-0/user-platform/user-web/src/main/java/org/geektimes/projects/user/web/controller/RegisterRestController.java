package org.geektimes.projects.user.web.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.web.mvc.RestResponse;
import org.geektimes.web.mvc.controller.RestController;

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

    private final UserService userService = new UserServiceImpl();

    @POST
    @Path("/register")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        final String email = request.getParameter("inputEmail");
        final String password = request.getParameter("inputPassword");

        System.out.println("email:" + email);
        System.out.println("password:" + password);

        User user = new User();
        user.setName(RandomStringUtils.random(5));
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(RandomStringUtils.random(11));

        try {
            if(userService.register(user)) {
                return RestResponse.success("home.jsp");
            } else {
                return RestResponse.failure(-1, "注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(-2, e.getMessage());
        }
    }
}
