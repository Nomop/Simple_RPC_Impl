package Common.serializer.mySerializer;

import java.io.*;

/**
 * @Description Java 内置的序列化机制实现的 Java 对象与字节数组的序列化和反序列化
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/22 9:12
 */
public class ObjectSerializer implements Serializer{

    //java io --> 字节数组
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        //字节存储容器
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            //对象输出流，用于将Java对象序列化的过渡容器，并连接bos
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            //对象序列化为字节数据
            //具体过程：ObjectOutputStream 会将对象的字段、类型信息等按照 Java 序列化协议编码为字节，并通过流写入到 ByteArrayOutputStream 的内部缓冲区中
            oos.writeObject(obj);
            //刷新对象输出流，确保所有缓冲区中的数据都写入底层流中
            oos.flush();
            //将bos其内部缓冲区中的数据转换为字节数组，并返回该数组
            bytes = bos.toByteArray();
            bos.close();
            oos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }


    //字节数组 --> 对象
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try{
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return obj;
    }


    //0 代表Java原生序列器
    @Override
    public int getType() {
        return 0;
    }
}
