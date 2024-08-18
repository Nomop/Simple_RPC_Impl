package Server.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Descriptio 本地服务存放器, 服务器端在接收到请求时能够快速查找并调用正确的服务实现
 * @Author nomo
 * @Version 1.0
 * @Date 2024/8/18 14:50
 */
public class ServiceProvider {

    private Map<String, Object> interfaceProvider;

    public ServiceProvider(){
        this.interfaceProvider =new HashMap<>();
    }

    //注册服务实现对象
    public void provideServiceInterface(Object service){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();


        for(Class<?> clazz : interfaceName){
            interfaceProvider.put(clazz.getName(),service);
        }
    }


    //根据接口名获取对应的服务实现对象实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
