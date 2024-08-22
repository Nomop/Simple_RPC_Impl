package Common.serializer.mySerializer;

import Common.Message.RpcRequest;
import Common.Message.RpcResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * @Description JSON序列器
 * @Author nomo
 * @Version 1.3
 * @Date 2024/8/22 9:12
 */
public class JsonSerializer implements Serializer{

    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }


    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        //传输消息包括Request和Respons
        switch (messageType){
            case 0:
                //将JSON反序列转换为对应对象，fastJSON可以读出基本类型，不用转换
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                Object[] objects = new Object[request.getParams().length];
                //对转换后的Request中的params属性逐个进行类型判断
                for(int i = 0; i < objects.length; i++){
                    Class<?> paramsType = request.getParamsType()[i];
                    //判断每个对象类型是否和paramsTypes中的一致
                    if(!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                        //如果不一致，就行进行类型转换
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i], request.getParamsType()[i]);
                    }else{
                        //如果一致，则直接赋值
                        objects[i] =request.getParams()[i];
                    }
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                RpcResponse response = JSON.parseObject(bytes,RpcResponse.class);
                Class<?> dataType = response.getDataType();
                //判断转化后的response对象中的data的类型是否正确
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject)response.getData(),dataType));
                }
                obj =response;
                break;
            default:
                System.out.println("暂时不支持此种消息");
                throw new RuntimeException();
        }
        return obj;
    }


    //1 代表JSON序列化方式
    @Override
    public int getType() {
        return 1;
    }
}
