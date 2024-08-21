package Server.serviceRegister;

import java.net.InetSocketAddress;

/**
 * @Description 服务注册接口
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/21 13:31
 */
public interface ServiceRegister {
    //注册保存服务与对应地址
    void register(String serviceName, InetSocketAddress serviceAddress);
}
