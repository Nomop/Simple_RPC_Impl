package Client.serviceCenter;

import com.google.common.base.Strings;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description 基于 Zookeeper 的服务注册与发现组件
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/21 11:12
 */
public class ZKServiceCenter implements ServiceCenter{
    // curator 提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";

    //zookeeper客户端初始化，并与服务端Zookeeper进行连接
    public ZKServiceCenter(){
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("zookeeper 连接成功");
    }

    //根据服务名（接口名）查询服务，返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try{
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            //默认返回服务的第一个子节点，后面可增加为负载均衡
            String string = strings.get(0);
            return parseAddress(string);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //字符串解析为 -> InetSocketAddress地址
    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0],Integer.parseInt(result[1]));
    }

    //地址格式化 -> 字符串XXX.XXX.XXX.XXX:port
    private String getServiceAddress(InetSocketAddress serviceAddress){
        return serviceAddress.getHostName() +
                ":" +
                serviceAddress.getPort();
    }
}
