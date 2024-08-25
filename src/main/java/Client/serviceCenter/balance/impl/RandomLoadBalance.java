package Client.serviceCenter.balance.impl;

import Client.serviceCenter.balance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @Description 随机分配 负载均衡 模拟
 * @Author nomo
 * @Version 1.5
 * @Date 2024/8/25 18:32
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("负载均衡选择" + choose + "服务器");
        return null;
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
