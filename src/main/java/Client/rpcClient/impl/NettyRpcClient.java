package Client.rpcClient.impl;

import Client.netty.NettyInitializer.NettyClientInitializer;
import Client.rpcClient.RpcClient;
import Common.Message.RpcRequest;
import Common.Message.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * @Description 通过 Netty 的异步非阻塞机制实现底层Rpc客户端通信
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/19 19:58
 */
public class NettyRpcClient implements RpcClient {
    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //netty客户端初始化
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                //NettyClientInitializer这里 配置netty对消息的处理机制
                .handler(new NettyClientInitializer());
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try{

            //创建channelFuture对象，相当于操作事件
            //sync()会阻塞当前线程，直到连接操作完成。
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //channel相当于socket，一个连接对象
            Channel channel = channelFuture.channel();
            //发送数据
            channel.writeAndFlush(request);
            //等待连接关闭
            channel.closeFuture().sync();

            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 当前场景下选择堵塞获取结果
            // 其它场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addListener...
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
