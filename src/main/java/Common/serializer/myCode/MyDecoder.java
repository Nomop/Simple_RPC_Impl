package Common.serializer.myCode;

import Common.Message.MessageType;
import Common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Description 自定义解码器，按照自定义的消息格式解码数据
 * @Author nomo
 * @Version 1.3
 * @Date 2024/8/22 10:25
 */
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //1.读取消息类型
        short messageType = in.readShort();
        // 现在还只支持request与response请求
        if(messageType != MessageType.REQUEST.getCode() && messageType != MessageType.RESPONSE.getCode()){
            System.out.println("暂时不支持此种数据");
            return;
        }
        //2.读取序列化方式和类型
        short serializerType = in.readShort();
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if(serializer == null){
            throw new RuntimeException("不存在对应的序列器");
        }
        //3.读取序列化数组长度
        short length = in.readShort();
        //4.读取序列化数组
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object deserialize = serializer.deserialize(bytes, messageType);
        out.add(deserialize);
    }
}
