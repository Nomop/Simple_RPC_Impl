package Client;

import Client.proxy.ClientProxy;
import Common.pojo.User;
import Common.service.UserService;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 9999);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端获取User："+user.toString());

        User user1 = User.builder().
                id(100)
                .userName("xiaohong")
                .sex(true)
                .build();
        Integer id = proxy.insertUserId(user1);
        System.out.println("向服务端传输UserId："+id);
    }
}
