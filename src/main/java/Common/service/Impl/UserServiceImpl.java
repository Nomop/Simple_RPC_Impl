package Common.service.Impl;


import Common.pojo.User;
import Common.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id) {
        //模拟服务端从数据库获取用户的行为
        System.out.println("客户端查询" + id + "的用户");
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean())
                .build();
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        //模拟向服务端接受客户端数据，向数据库插入数据
        System.out.println("客户端插入数据成功：" + user.getUserName());
        return user.getId();
    }
}
