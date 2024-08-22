package Common.serializer.mySerializer;

/**
 * @Description 序列器接口
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/22 9:08
 */
public interface Serializer {
    //对象序列化为字节数组
    byte[] serialize(Object obj);

    //字节数组反序列化为消息
    //使用java自带序列化方式不用messageType也能得到相应的对象（序列化字节数组里包含类信息）
    //其他方式需要指定消息格式，再根据message转换成响应对象
    Object deserialize(byte[] bytes, int messageType);

    //返回使用的序列器
    //0：Java自带序列化方，1：JSON序列化方式
    int getType();

    //静态方法，根据序号取出序列化器，暂时有两种实现，可拓展
    static Serializer getSerializerByCode(int code){
        switch(code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
