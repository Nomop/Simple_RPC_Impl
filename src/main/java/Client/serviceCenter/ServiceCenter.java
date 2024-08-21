package Client.serviceCenter;

import java.net.InetSocketAddress;

/**
 * @Description 服务中心接口
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/21 11:10
 */
public interface ServiceCenter {
    //根据服务名查询地址
    InetSocketAddress serviceDiscovery(String serviceName);
}
