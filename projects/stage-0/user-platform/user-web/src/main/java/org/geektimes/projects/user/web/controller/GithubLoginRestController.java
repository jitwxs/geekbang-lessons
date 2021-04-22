package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.service.OauthService;
import org.geektimes.rest.bean.RestResponse;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author xiangsheng.wu
 * @date 2021-04-22 12:03
 */
@Path("/")
public class GithubLoginRestController implements RestController {
    private static String GITHUB_CLIENT_ID = "0307dc634e4c5523cef2";
    private static String GITHUB_CLIENT_SECRET = "707647176eb3bef1d4c2a50fcabf73e0401cc877";
    private static String GITHUB_REDIRECT_URL = "http://127.0.0.1:8080/githubCallback";

    @GET
    @Path("githubLogin")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        // Github认证服务器地址
        String url = "https://github.com/login/oauth/authorize";
        // 生成并保存state，忽略该参数有可能导致CSRF攻击
        String state = OauthService.genState();
        // 传递参数response_type、client_id、state、redirect_uri
        String param = "response_type=code&" + "client_id=" + GITHUB_CLIENT_ID + "&state=" + state
                + "&redirect_uri=" + GITHUB_REDIRECT_URL;

        // 1、请求Github认证服务器
        response.sendRedirect(url + "?" + param);

        return null;
    }
}
