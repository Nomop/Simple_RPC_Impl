package Client.retry;

import Client.rpcClient.RpcClient;
import Common.Message.RpcRequest;
import Common.Message.RpcResponse;
import com.github.rholder.retry.*;


import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @Description  Guava 库中的重试机制来实现 RPC 服务调用的重试逻辑
 * @Author nomo
 * @Version 1.6
 * @Date 2024/8/26 14:10
 */
public class guaveRetry {
    private RpcClient rpcClient;
    public RpcResponse sendServiceWithRetry(RpcRequest request, RpcClient rpcClient){
        this.rpcClient = rpcClient;
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                //无论遇到什么异常，都会触发重试
                .retryIfException()
                //如果返回的 RpcResponse 对象的 code 为 500，表示服务错误，将会触发重试。
                .retryIfResult(response -> Objects.equals(response.getCode(),500))
                //每次重试之间等待 2 秒
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //最多重试 3 次后停止重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                //重试监听器，用于在每次重试时输出重试的尝试次数
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("RetryListener:第" + attempt.getAttemptNumber() + "次调用");
                    }
                })
                .build();
        try {
            return retryer.call(() -> rpcClient.sendRequest(request));
        }catch (Exception e){
            e.printStackTrace();
        }

        return RpcResponse.fail();
    }
}
