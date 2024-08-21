package Server.serviceRegister.impl;


import Server.serviceRegister.ServiceRegister;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;

/**
 * @Description 使用 Zookeeper 作为服务注册中心
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/21 13:32
 */
public class ZKServiceRegister implements ServiceRegister {
    // curator（一个简化的 Zookeeper 客户端） 提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper虚拟根路径节点
    private static final String ROOT_PATH = "MyRPC";

    //zookeeper客户端初始化，并与服务端Zookeeper进行连接
    public ZKServiceRegister(){
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

    //将服务注册到注册中心
    @Override
    public void register(String serviceName, InetSocketAddress serviceAddress) {
        try{
            //如果不存在服务名称对应的节点，serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if(client.checkExists().forPath("/" + serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            //路径地址，一个/代表一个节点
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            //在serviceName下，创建临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        }catch (Exception e){
            System.out.println("此服务已存在");
        }

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
