package com.geshanzsq.framework.web.service;

import com.geshanzsq.common.constant.Constants;
import com.geshanzsq.common.constant.UserConstants;
import com.geshanzsq.common.core.domain.entity.SysUser;
import com.geshanzsq.common.core.domain.model.RegisterBody;
import com.geshanzsq.common.core.redis.RedisCache;
import com.geshanzsq.common.exception.user.CaptchaException;
import com.geshanzsq.common.exception.user.CaptchaExpireException;
import com.geshanzsq.common.utils.SecurityUtils;
import com.geshanzsq.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//用户注册校验
@Component
public class SysRegisterService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    //注册用户
    public String register(RegisterBody user){
        //校验用户输入验证码
        validateCaptcha(user.getUsername(),user.getCode(),user.getUuid());

        //校验username是否唯一
        if(userService.checkUserNameUnique(user.getUsername()).equals(UserConstants.NOT_UNIQUE)){
            return "用户名"+user.getUsername()+"已存在!";
        }

        SysUser newUser = new SysUser();
        newUser.setUserName(user.getUsername());
        newUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        newUser.setNickName(user.getNickName());
        newUser.setEmail(user.getEmail());
        newUser.setPhonenumber(user.getPhoneNumber());

        if(userService.insertUser(newUser) != 1){
            return "Mysql新增用户失败!";
        }

        return "";

    }


    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
