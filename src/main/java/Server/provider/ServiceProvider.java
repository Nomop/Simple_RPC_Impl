package Server.provider;

import Server.serviceRegister.ServiceRegister;
import Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Descriptio 本地服务存放器, 服务器端在接收到请求时能够快速查找并调用正确的服务实现
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/18 14:50
 */
public class ServiceProvider {

    private Map<String, Object> interfaceProvider;
    private String host;
    private int port;

    //注册服务类
    private ServiceRegister serviceRegister;

    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
    }

    //注册服务实现对象
    public void provideServiceInterface(Object service){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for(Class<?> clazz : interfaceName){
            //本机的映射表
            interfaceProvider.put(clazz.getName(),service);
            //注册中心进行注册
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host,port));
        }
    }


    //根据接口名获取对应的服务实现对象实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
