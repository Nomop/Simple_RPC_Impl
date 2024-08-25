package Client.serviceCenter.balance.impl;

import Client.serviceCenter.balance.LoadBalance;

import java.util.List;

/**
 * @Description 一致性哈希算法 负载均衡
 * @Author nomo
 * @Version 1.5
 * @Date 2024/8/25 18:30
 */
public class ConsistencyHashBalance implements LoadBalance {
    @Override
    public String balance(List<String> addressList) {
        return null;
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {
        
    }
}
