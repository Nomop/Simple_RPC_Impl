package Client.serviceCenter;

import Client.cache.serviceCache;
import Client.serviceCenter.ZkWatcher.watchZK;
import Client.serviceCenter.balance.impl.ConsistencyHashBalance;
import com.google.common.base.Strings;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description 基于 Zookeeper 的服务注册与发现组件
 * 1.4 增加本地缓存
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/21 11:12
 */
public class ZKServiceCenter implements ServiceCenter{
    // curator（一个简化的 Zookeeper 客户端） 提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper虚拟根路径节点
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";
    //本地缓存
    private serviceCache cache;

    //zookeeper客户端初始化，并与服务端Zookeeper进行连接
    public ZKServiceCenter() throws InterruptedException {
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

        //初始化本地缓存
        cache =  new serviceCache();
        //加入ZK监听器
        watchZK watcher = new watchZK(client, cache);
        //监听启动
        watcher.watchToUpdate(ROOT_PATH);
    }

    //根据服务名（接口名）查询服务，返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try{
            //先从本地缓存找
            List<String> serviceList = cache.getServiceFromCache(serviceName);
            //如果找不到，再去zookeeper中找
            //这种i情况基本不会发生，或者说只会出现在初始化阶段
            if(serviceList == null){
                serviceList = client.getChildren().forPath("/" + serviceName);
            }
            //负载均衡得到地址
            String address = new ConsistencyHashBalance().balance(serviceList);
            return parseAddress(address);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //判断一个服务名称是否在重试白名单中
    public boolean checkRetry(String serviceName){
        boolean canRetry = false;
        try{
            List<String> serviceList = client.getChildren().forPath("/" + RETRY);
            for(String s: serviceList){
                if(s.equals(serviceName)){
                    System.out.println("服务" + serviceName + "在白名单上，可进行重试");
                    canRetry = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return canRetry;
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
