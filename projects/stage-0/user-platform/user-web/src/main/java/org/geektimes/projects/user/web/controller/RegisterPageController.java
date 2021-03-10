package org.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author jitwxs
 * @date 2021年03月06日 16:53
 */
@Path("/")
public class RegisterPageController implements PageController {

    /**
     * 跳转到注册界面（使用登录界面模拟）
     */
    @GET
    @Path("register")
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "login-form.jsp";
    }
}
