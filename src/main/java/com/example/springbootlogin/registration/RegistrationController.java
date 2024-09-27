package com.example.springbootlogin.registration;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
	1.	RegistrationController：作为控制器，它接收 HTTP 请求，并将请求数据交给业务层（就是服务层@Service）处理。它是注册流程的入口。
	2.	RegistrationRequest：封装了客户端发来的注册数据，作为数据载体，便于服务层处理。
	3.	RegistrationService：负责处理注册的实际业务逻辑。尽管现在只是简单地返回了 "works"，但最终它会包含数据验证、存储、响应结果等业务处理步骤。
 */

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }
}
