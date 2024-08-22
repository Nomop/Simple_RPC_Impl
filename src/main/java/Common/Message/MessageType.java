package Common.Message;

import lombok.AllArgsConstructor;

/**
 * @Description 枚举类，为每种消息类型赋予了一个整数代码
 * @Author nomo
 * @Version 1.2
 * @Date 2024/8/22 9:02
 */
@AllArgsConstructor
public enum MessageType {
    REQUEST(0),RESPONSE(1);//枚举常量
    private int code;
    public int getCode(){
        return code;
    }
}
