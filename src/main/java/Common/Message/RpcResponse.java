package Common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.io.Serializable;

/**
 * @Description 定义响应消息格式式
 * 1.3 增加构造器，fastJSON需要构建消息实例.
 *     增加数据类型dataType,更新success方法，解码器检查数据类型
 * @author nomo
 * @version 1.0
 * @create 2024/8/18 14:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
                .dataType(data.getClass())
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
