package com.yuantu.dev.sys.controller;

import com.baomidou.kisso.security.token.SSOToken;
import com.yuantu.dev.common.annotation.LogTrack;
import com.yuantu.dev.common.utils.JacksonUtils;
import com.yuantu.dev.common.web.Account;
import com.yuantu.dev.common.web.LoginHelper;
import com.yuantu.dev.sys.dto.LoginDTO;
import com.yuantu.dev.sys.entity.Log;
import com.yuantu.dev.sys.entity.User;
import com.yuantu.dev.sys.service.ILogService;
import com.yuantu.dev.sys.service.IUserService;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Login;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 单点登录
 * </p>
 *
 * @author jobob
 * @since 2018-10-12
 */
@Api(tags = {"单点登录"})
@RestController
@RequestMapping("/sso")
public class SSOController extends ApiController {
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILogService logService;


    /**
     * 用户登录
     */
    @LogTrack("登录")
    @Login(action = Action.Skip)
    @PostMapping("/login")
    public R<User> login(LoginDTO dto) {
        //Assert.fail(!"102400".equals(request.getParameter("signal")), "暗号不正确！");
        return success(userService.loginByDto(request, response, dto));
    }


    /**
     * 用户退出
     */
    @LogTrack("退出")
    @Login(action = Action.Skip)
    @GetMapping("/logout")
    public void logout() {
        try {
            Account account = LoginHelper.getAccount(false);
            Log log = new Log();
            log.setUserId(account.getId());
            log.setUsername(account.getName());
            log.setUri(request.getRequestURI());
            log.setIp(request.getRemoteAddr());
            log.setParams("logout:" + JacksonUtils.toJSONString(request.getParameterMap()));
            log.setRemark("退出");
            logService.add(log);
            SSOHelper.logout(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
