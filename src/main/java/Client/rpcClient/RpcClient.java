package Client.rpcClient;

import Common.Message.RpcRequest;
import Common.Message.RpcResponse;

/**
 * @Description
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/19 19:44
 */
public interface RpcClient {

    //定义底层通信方法
    RpcResponse sendRequest(RpcRequest request);
}
