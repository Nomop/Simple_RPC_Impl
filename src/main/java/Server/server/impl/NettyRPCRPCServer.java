package Server.server.impl;

import Server.netty.nettyInitializer.NettyServerInitializer;
import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;

/**
 * @Description 基于 Netty 框架的 RPC 服务器
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/21 8:19
 */
@AllArgsConstructor
public class NettyRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        //Netty服务线程：boss负责处理客户端的连接请求，Work负责处理已经建立的连接中的数据传输
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.println("Netty服务端启动");
        try{
            //引导和启动服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //初始化
            serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    //NettyClientInitializer这里 配置netty对消息的处理机制
                    .childHandler(new NettyServerInitializer(serviceProvider));
            //绑定端口，同步阻塞
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //死循环监听
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }


    @Override
    public void stop() {

    }
}
