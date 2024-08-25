package Client.serviceCenter.balance;

import java.util.List;

/**
 * @Description 给服务地址列表，根据不同的负载均衡策略选择一个
 * @Author nomo
 * @Version 1.5
 * @Date 2024/8/25 18:24
 */
public interface LoadBalance {
    String balance(List<String> addressList);
    void addNode(String node);
    void delNode(String node);
}
