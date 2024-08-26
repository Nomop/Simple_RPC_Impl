package Client.serviceCenter.balance.impl;

import Client.serviceCenter.balance.LoadBalance;

import java.util.*;

/**
 * @Description 一致性哈希算法 负载均衡
 * @Author nomo
 * @Version 1.5
 * @Date 2024/8/25 18:30
 */
public class ConsistencyHashBalance implements LoadBalance {
    //虚拟节点数量
    private static final int VIRTUAL_NUM = 5;
    // 虚拟节点分配，key是hash值，value是虚拟节点服务器名称
    private SortedMap<Integer,String> shards = new TreeMap<>();
    // 真实节点列表
    private List<String> realNodes = new LinkedList<>();
    //模拟初始服务器
    private String[] servers = null;

    /**
     * FNV1_32_HASH算法
     * @param str
     * @return
     */
    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++){
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if(hash < 0){
            hash = Math.abs(hash);
        }
        return hash;
    }


    private void init(List<String> serviceList){
        for (String server: serviceList){
            realNodes.add(server);
            System.out.println("真实节点【" + server + "】被添加");

            for(int i = 0; i < VIRTUAL_NUM; i++){
                String virtualNode = server + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash,virtualNode);
                System.out.println("虚拟节点【" + virtualNode + "】 hash:" + hash +"，被添加");
            }
        }
    }

    /**
     * 从哈希环获取虚拟节点
     * @param node
     * @param serviceList
     * @return
     */
    public String getServer(String node, List<String> serviceList){
        init(serviceList);
        int hash = getHash(node);
        Integer key = null;
        //获取比 hash 值大的所有虚拟节点
        SortedMap<Integer, String> subMap = shards.tailMap(hash);
        if(subMap.isEmpty()){
            key = shards.lastKey();
        }else {
            key = subMap.firstKey();
        }
        String virtualNode = shards.get(key);
        return virtualNode.substring(0,virtualNode.indexOf("&&"));
    }


    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        return getServer(random,addressList);
    }

    /**
     * 添加节点
     * @param node
     */
    @Override
    public void addNode(String node) {
        if(!realNodes.contains(node)){
            realNodes.add(node);
            System.out.println("真实节点【" + node + "】 上线添加");
            for(int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash,virtualNode);
                System.out.println("虚拟节点【" + virtualNode + "】 hash:" + hash + "，下线添加");
            }
        }
    }

    /**
     * 删除节点
     * @param node
     */
    @Override
    public void delNode(String node) {
        if(realNodes.contains(node)) {
            realNodes.remove(node);
            System.out.println("真实节点【" + node + "】 下线移除");
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点【" + virtualNode + "】 hash:" + hash + "，下线移除");
            }
        }
    }
}
