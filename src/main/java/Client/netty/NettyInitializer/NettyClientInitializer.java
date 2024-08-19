package Client.netty.NettyInitializer;

import Client.netty.handler.NettyClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @Description Netty 客户端初始化器类，为每个新的客户端连接配置一系列的 ChannelHandler
 * @Author nomo
 * @Version 1.1
 * @Date 2024/8/19 20:10
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //定义消息格式【长度】【消息体】，解决粘包问题，
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        //计算当前待发送消息长度，写入前四个字节
        pipeline.addLast(new LengthFieldPrepender(4));

        //编码器
        //使用java序列化方式，netty自带的解编码器支持传输这种结构
        pipeline.addLast(new ObjectEncoder());
        //解码器
        //使用Netty的ObjectDecoder,它将字节流解码为Java对象
        //在ObjectDecoder的构造函数中传入了一个ClassResolver 对象，用于解析类名并加载相应的类。
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        pipeline.addLast(new NettyClientHandler());
    }
}
