package com.flyingdata.core.utils;

import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public class FastjsonUtil {

    public static String toJSONString(Object o) {
        return JSON.toJSONString(o, SerializerFeature.WriteMapNullValue);
    }

    public static JsonData toEsJsonData(Object o) {
        return JsonData.fromJson(toJSONString(o));
    }
}
