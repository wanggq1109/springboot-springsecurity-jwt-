package com.maomiyibian.microservice.provider.impl.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.maomiyibian.microservice.api.domain.user.User;
import com.maomiyibian.microservice.api.service.user.UserService;
import com.maomiyibian.microservice.common.message.TradeMessages;
import com.maomiyibian.microservice.common.page.Page;
import com.maomiyibian.microservice.provider.template.DataServiceMybatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * TODO: 用户接口实现类
 *
 * @author junyunxiao
 * @date 2018-9-10 9:36
 */
@Service(
        version = "${service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class UserServiceImpl implements UserService {

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

  /*  @Autowired
    private PasswordEncoder passwordEncoder;*/

    @Autowired
    private DataServiceMybatis dataServiceStat;

    @Override
    public boolean unboundUser(User user) {
        return false;
    }

    @Override
    public boolean boundUser(User user) {
        return false;
    }

    @Override
    public boolean updateUserInfo(User user) {
        return false;
    }

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Override
    public TradeMessages<String> userRegister(User user) {
        TradeMessages<String> messages=new TradeMessages<>();
        long userId=System.currentTimeMillis();
        user.setId(userId);
        user.setUserPwd(user.getUserPwd());
        try {
            dataServiceStat.insert("com.maomiyibian.microservice.provider.dao.user.UserDao.userRegister",user);
            messages.setData(null);
            messages.setResultCode("100200");
            messages.setResultMessage("用户注册成功");
        }catch (Exception e){
            messages.setData(null);
            messages.setResultCode("100400");
            messages.setResultMessage("用户注册失败");
        }
        return messages;
    }

    @Override
    public User queryUserByName(String userName) {
        logger.info("com.maomiyibian.microservice.provider.impl.UserServiceImpl.queryUserByName:Rpc调用开始");
        return dataServiceStat.getObject("com.maomiyibian.microservice.provider.dao.user.UserDao.queryUserByName",userName);
    }

    @Override
    public User queryUser(Map<String, Object> parmeter) {
        logger.info("com.maomiyibian.microservice.provider.impl.UserServiceImpl.queryUser:Rpc调用开始");
        return dataServiceStat.getObject("com.maomiyibian.microservice.provider.dao.user.UserDao.queryUser",parmeter);
    }

    @Override
    public Page<User> queryUserByPage(Object parameter,Page page) throws Exception {
        logger.info("com.maomiyibian.microservice.provider.impl.UserServiceImpl.queryUserByPage:Rpc调用开始");
        return dataServiceStat.query("com.maomiyibian.microservice.provider.dao.user.UserDao.queryUserByPage","com.maomiyibian.microservice.provider.dao.user.UserDao.queryUserCount",parameter,page);
    }

}
