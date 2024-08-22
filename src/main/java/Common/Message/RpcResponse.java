package Common.Message;

import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.io.Serializable;

/**
 * @author nomo
 * @version 1.0
 * @create 2024/8/18 14:24
 *定义响应消息格式
 */
@Data
@Builder
public class RpcResponse implements Serializable {

    //状态信息和具体数据
    private int code;
    private String msg;
    private Object data;

    //增加传输数据类型，用于序列器解析
    private Class<?> dataType;

    //构造成功信息
    public static RpcResponse success(Object data){
        return RpcResponse.builder()
                .code(200)
                .data(data)
                .build();
    }

    //构造失败信息
    public static RpcResponse fail(){
        return RpcResponse.builder()
                .code(500)
                .msg("服务器发生错误")
                .build();
    }
}
