package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.ToolCase.dao.SysUserMapper;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysUser;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.TypeChange.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysSafetyServiceImpl implements SysSafetyService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;

    @Override
    public ResultBean loginSystem(String userId, String password, HttpServletResponse servletResponse) {
        log.info("[{}]用戶申请登录系统", userId);
        try {
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("user_id", userId));
            if (sysUser == null) {
                log.info("系统不存在该用户[{}]", userId);
                return ResultUtil.fail("系统不存在该用户");
            } else {
                String md5 = MD5Utils.convertMD5(sysUser.getPassword());
                //log.info("md5[{}]",md5);
                if (password.equals(md5)) { //校验密码
                    log.info("[{}]用戶密码校验通过", userId);
                    sysUser.setPassword(null);//清空密码
                    sysUser.setCreatedTime(null);
                    sysUser.setUpdatedTime(null);
                    String token = userId + UUID.randomUUID().toString(); //目前采用userId（手机号） + uuid作为系统的token，后期严格了再换
                    String userKey = "COM:USER:" + token;
                    Set keys = redisUtil.keys("COM:USER:" + userId + "*");
                    if (!keys.isEmpty()) { //缓存中存在,之前有用户登录过
                        redisUtil.del(keys.iterator().next().toString()); //清除之前登录的key
                        log.info("[{}]用户异地登录，清除上次登录token", userId);
                    }
                    Cookie cookie = new Cookie("COMPASS_TOKEN", token);
                    cookie.setMaxAge(-1);//设置Cookie为关闭浏览器失效
                    cookie.setPath("/");
                    servletResponse.addCookie(cookie);//注入系統的token
                    redisUtil.set(userKey, sysUser, TimeUnit.MINUTES.toSeconds(SSO_SESSION_EXPIRE));
                    return ResultUtil.success(sysUser);
                } else {
                    log.info("[{}]用户密码校验不通过", userId);
                    return ResultUtil.fail("用户或密码错误");
                }
            }
        } catch (Exception e) {
            log.error("[]用户登录发生异常", userId, e);
            return ResultUtil.error(1004, "登录异常");
        }
    }

    @Override
    public SysUser getCookieUserInfo(String cookie) {
        log.info("获取Cookie[{}]对应用户信息", cookie);
        SysUser sysUser = (SysUser) redisUtil.get("COM:USER:" + cookie);
        return sysUser;
    }

    @Override
    public ResultBean loginoutSystem(String cookie) {
        redisUtil.del("COM:USER:" + cookie);
        return ResultUtil.success(null, "成功登出系统!");
    }

}
