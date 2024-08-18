package Common.service;

import Common.pojo.User;

/**
 * @author nomo
 * @version 1.0
 * @create 2024/8/18 14:24
 */
public interface UserService {
    //客户端通过此接口调用服务端实现类
    User getUserByUserId(Integer id);
    //增加新的接口，一个插入功能
    Integer insertUserId(User user);
}
