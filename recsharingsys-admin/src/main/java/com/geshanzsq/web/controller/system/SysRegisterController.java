package com.geshanzsq.web.controller.system;

import com.geshanzsq.common.core.controller.BaseController;
import com.geshanzsq.common.core.domain.AjaxResult;
import com.geshanzsq.common.core.domain.model.RegisterBody;
import com.geshanzsq.framework.web.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysRegisterController extends BaseController {

    @Autowired
    private SysRegisterService registerService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user){
        String msg = registerService.register(user);

        return msg.equals("")? AjaxResult.success() : AjaxResult.error(msg);

    }


}
