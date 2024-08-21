package Common.Message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author nomo
 * @version 1.0
 * @create 2024/8/18 14:24
 * 定义发送消息格式
 */
@Data
@Builder
public class RpcRequest implements Serializable {

    //服务类名，客户端只知道接口
    private String interfaceName;
    //调用方法名
    private String methodName;
    //参数列表
    private Object[] params;
    //参数类型
    private Class<?>[] paramsType;
}
