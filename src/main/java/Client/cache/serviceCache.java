package Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 本地缓存管理器
 * @Author nomo
 * @Version 1.4
 * @Date 2024/8/24 14:29
 */
public class serviceCache {
    //map存放缓存
    //key: serviceName 服务名
    //value： addressList 服务提供者列表
    private static Map<String, List<String>> cache =new HashMap<>();

    //添加服务到缓存中
    public void addServiceToCache(String  serviceName,String address){
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将Name为" + serviceName + "和地址为" + addressList + "的服务添加到本地缓存中");
        }else{
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName,addressList);
//            System.out.println("将Name为" + serviceName + "和地址为" + addressList + "的新服务添加到本地缓存中");
        }
    }

    //修改缓存中服务地址
    public void replaceServiceAddress(String serviceName, String newAddress, String oldAddress){
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.remove(oldAddress);
            addressList.add(newAddress);
        }else {
            System.out.println("修改服务失败，服务不存在");
        }
    }

    //删除缓存中服务地址
    public void delete(String serviceName, String address){
        List<String> addressList = cache.get(serviceName);
        addressList.remove(address);
        System.out.println("将Name为" + serviceName + "和地址为" + addressList + "的服务从本地缓存中删除");
    }

    //从缓存获取服务地址
    public List<String> getServiceFromCache(String seriveName){
        if(!cache.containsKey(seriveName)){
            return null;
        }else{
            List<String> addressList = cache.get(seriveName);
            return addressList;
        }
    }
}
