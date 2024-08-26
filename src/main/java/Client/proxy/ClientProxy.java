package Client.proxy;

import Client.retry.guaveRetry;
import Client.rpcClient.RpcClient;
import Client.rpcClient.impl.NettyRpcClient;
import Client.serviceCenter.ServiceCenter;
import Client.serviceCenter.ZKServiceCenter;
import Common.Message.RpcRequest;
import Common.Message.RpcResponse;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description 客户端代理类
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/18 15:15
 */
public class ClientProxy implements InvocationHandler {

    //服务端地址,封装到rpcClient内部，直接通过代理使用客户端就行
    //从注册中心获取host和port，
    private RpcClient rpcClient;

    private ServiceCenter serviceCenter;
    
    public ClientProxy() throws InterruptedException {
        serviceCenter = new ZKServiceCenter();
        rpcClient = new NettyRpcClient(serviceCenter);
    }


    //代理对象拦截，在invoke增强执行
    //具体来说，进行反射获取request对象，socket发送到服务端
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //构建Request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsType(method.getParameterTypes())
                .build();

        //发送Request，和服务端进行数据传输
        RpcResponse response;
        //后续添加逻辑：为保持幂等性，只对白名单上的服务进行重试
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            //调用retry框架进行重试操作
            response = new guaveRetry().sendServiceWithRetry(request,rpcClient);
        }else{
            //只调用一次
            response = rpcClient.sendRequest(request);
        }
        
        return response.getData();
    }

    //动态生成一个实现了指定接口的代理对象
    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }


}
