package Client.serviceCenter.balance.impl;

import Client.serviceCenter.balance.LoadBalance;

import java.util.List;

/**
 * @Description
 * @Author nomo
 * @Version 1.5
 * @Date 2024/8/25 18:32
 */
public class RandomLoadBalance implements LoadBalance {

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
