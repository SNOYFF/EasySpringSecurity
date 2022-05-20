package open.example.easyspringsecurity.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * json工具类
 */
public class GsonUtil {

    /**
     * 私有构造方法
     */
    private GsonUtil(){}

    /**
     * 全局Gson
     */
    private static final Gson GSON = new Gson();

    /**
     * 转化为JSON
     * @param object 需要处理的对象
     */
    public static String toJson(Object object){
        return GSON.toJson(object);
    }

    /**
     * 转化为对象
     * @param json   原Json数据
     * @param tClass 转化为的Json对象
     * @param <T>    对象类型
     */
    public static <T> T fromJson(String json,Class<T> tClass){
        return GSON.fromJson(json, tClass);
    }

    /**
     * json反序列化
     * @param json json数据
     * @param type 类型
     * @param <T>  返回的数据
     */
    public static <T> T fromJson(String json, Type type){
        return GSON.fromJson(json,type);
    }

}
