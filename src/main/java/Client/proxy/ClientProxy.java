package Client.proxy;

import Client.IOClient;
import Common.Message.RpcRequest;
import Common.Message.RpcResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description 客户端代理类
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/18 15:15
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    //服务端地址
    private String host;
    private int port;

    //代理对象拦截，在invoke增强执行
    //具体来说，进行反射获取request对象，socket发送到服务端
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //构建Request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methonName(method.getName())
                .params(args)
                .paramsType(method.getParameterTypes())
                .build();

        //发送Request，和服务端进行数据传输
        RpcResponse response = IOClient.sendRequest(host, port, request);
        return response.getData();
    }

    //动态生成一个实现了指定接口的代理对象
    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }


}
