package com.silwings.responder.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.silwings.responder.commons.exception.ResponderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * bean属性复制功能，如果有异常会返回具体是哪个属性的setter方法出现异常
 */
public class BeanCopyUtils extends BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanCopyUtils.class);

    private static final String EXCEPTION_LOG_FORMAT_COPYPROPS = "BEAN COPY Exception! methodName=%s ; %";

    /**
     * 基于JSON转换的JavaBean对象复制(隐含基本数据类型转换，支持属性重新命名)
     *
     * @param source     原数据对象
     * @param type       目标JavaBean类型
     * @param keyMapping 需要转换的key对照表, 例如: {<data.nodeO.oldCode, data.nodeT.newCode>}
     */
    public static <T> T jsonCopyBean(Object source, Type type, Map<String, String> keyMapping) {

        if (null == source) {
            return null;
        }

        // 先转换成Map
        Object jsonObject = JSON.toJSON(source);

        // 转换键名
        convertKeys(jsonObject, keyMapping);

        return jsonCopyBean(JSON.toJSONString(jsonObject), type);
    }

    public static <T> T jsonCopyBean(Object source, Class<T> targetClazz, Map<String, String> keyMapping) {

        return jsonCopyBean(source, (Type) targetClazz, keyMapping);
    }

    public static <T> T jsonCopyBean(Object source, TypeReference<T> type, Map<String, String> keyMapping) {

        return jsonCopyBean(source, type.getType(), keyMapping);
    }

    /**
     * 基于JSON转换的JavaBean对象复制(隐含基本数据类型转换，支持属性重新命名)， 合并source1和source2的属性，赋值给目标对象，相同属性时source1优先级更高，忽略为null的字段
     *
     * @param source1    JavaBean对象
     * @param source2    JavaBean对象
     * @param type       目标JavaBean类型
     * @param keyMapping 需要转换的key对照表, 例如: {<data.nodeO.oldCode, data.nodeT.newCode>}
     */
    public static <T> T jsonMergeBean(Object source1, Object source2, Type type, Map<String, String> keyMapping) {

        if (null == source1) {
            return jsonCopyBean(source2, type, keyMapping);
        }
        if (null == source2) {
            return jsonCopyBean(source1, type, keyMapping);
        }

        // 先转换成Map
        JSONObject jsonObject = mergeJsonObject(source1, source2);

        // 转换键名
        convertKeys(jsonObject, keyMapping);

        return jsonCopyBean(JSON.toJSONString(jsonObject), type);
    }

    public static <T> T jsonMergeBean(Object source1, Object source2, Class<T> targetClazz, Map<String, String> keyMapping) {

        return jsonMergeBean(source1, source2, (Type) targetClazz, keyMapping);
    }

    public static <T> T jsonMergeBean(Object source1, Object source2, TypeReference<T> type, Map<String, String> keyMapping) {

        return jsonMergeBean(source1, source2, type.getType(), keyMapping);
    }

    /**
     * 基于JSON转换的JavaBean对象复制(隐含基本数据类型转换), 合并source1和source2的属性，赋值给目标对象，相同属性时source1优先级更高，忽略为null的字段
     *
     * @param source1 JavaBean对象
     * @param source2 JavaBean对象
     * @param type    目标JavaBean类型
     * @return 目标JavaBean对象
     * @author Andrew.Dong
     */
    public static <T> T jsonMergeBean(Object source1, Object source2, Type type) {

        if (null == source1) {
            return jsonCopyBean(source2, type);
        }
        if (null == source2) {
            return jsonCopyBean(source1, type);
        }

        return jsonCopyBean(JSON.toJSONString(mergeJsonObject(source1, source2)), type);
    }

    public static <T> T jsonMergeBean(Object source1, Object source2, Class<T> targetClazz) {

        return jsonMergeBean(source1, source2, (Type) targetClazz);
    }

    public static <T> T jsonMergeBean(Object source1, Object source2, TypeReference<T> type) {

        return jsonMergeBean(source1, source2, type.getType());
    }

    private static JSONObject mergeJsonObject(Object source1, Object source2) {

        JSONObject jsonObject = (JSONObject) JSON.toJSON(source2);
        for (Entry<String, Object> entiy : ((JSONObject) JSON.toJSON(source1)).entrySet()) {
            if (entiy.getValue() != null) {
                jsonObject.put(entiy.getKey(), entiy.getValue());
            }
        }

        return jsonObject;
    }

    /**
     * 基于JSON转换的JavaBean对象复制(隐含基本数据类型转换，支持属性重新命名)
     *
     * @param source     原数据对象
     * @param type       目标JavaBean类型
     * @param keyMapping 需要转换的key对照表, 例如: {<data.nodeO.oldCode, data.nodeT.newCode>}
     */
    public static <T> List<T> jsonCopyList(Collection<Object> source, Type type, Map<String, String> keyMapping) {

        if (null == source || source.isEmpty()) {
            return new ArrayList<>();
        }

        // 先转换成Map
        Object jsonObject = JSON.toJSON(source);

        // 转换键名
        convertKeys(jsonObject, keyMapping);

        return jsonCopyList(JSON.toJSONString(jsonObject), type);
    }

    public static <T> List<T> jsonCopyList(Collection<Object> source, Class<T> targetClazz, Map<String, String> keyMapping) {

        return jsonCopyList(source, (Type) targetClazz, keyMapping);
    }

    public static <T> List<T> jsonCopyList(Collection<Object> source, TypeReference<T> type, Map<String, String> keyMapping) {

        return jsonCopyList(source, type.getType(), keyMapping);
    }

    /**
     * 基于JSON转换的JavaBean对象复制(隐含基本数据类型转换)
     *
     * @param source JavaBean对象
     * @param type   目标JavaBean类型
     * @return 目标JavaBean对象
     * @author Andrew.Dong
     */
    public static <T> T jsonCopyBean(Object source, Type type) {

        if (null == source) {
            return null;
        }
        if (source instanceof String) {
            return JSON.parseObject((String) source, type);
        }

        return JSON.parseObject(JSON.toJSONString(source), type);
    }

    public static <T> T jsonCopyBean(Object source, Class<T> targetClazz) {

        return jsonCopyBean(source, (Type) targetClazz);
    }

    public static <T> T jsonCopyBean(Object source, TypeReference<T> type) {

        return jsonCopyBean(source, type.getType());
    }

    /**
     * 基于JSON转换的JavaBean对象复制(隐含基本数据类型转换)
     *
     * @param source JavaBean对象
     * @param type   目标JavaBean类型
     * @return 目标JavaBean对象
     * @author Andrew.Dong
     */
    public static <T> List<T> jsonCopyList(Object source, Type type) {

        if (null == source) {
            return new ArrayList<>();
        }

        String stringSource = null;

        if (source instanceof String) {
            stringSource = (String) source;
        } else {
            stringSource = JSON.toJSONString(source);
        }

        if (StringUtils.isBlank(stringSource)) {
            return new ArrayList<>();
        }

        try (DefaultJSONParser parser = new DefaultJSONParser(stringSource, ParserConfig.global)) {
            List<T> resultList = new ArrayList<>();
            parser.parseArray(type, resultList);
            parser.handleResovleTask(resultList);
            return resultList;
        }
    }

    public static <T> List<T> jsonCopyList(Object source, Class<T> targetClazz) {

        return jsonCopyList(source, (Type) targetClazz);
    }

    public static <T> List<T> jsonCopyList(Object source, TypeReference<T> type) {

        return jsonCopyList(source, type.getType());
    }

    /**
     * 转换JSON对象中的键值名称
     *
     * @param obj        JSON对象(Map or List)
     * @param keyMapping 新旧键名对照(Ex: <data.nodeO.oldCode, data.nodeT.newCode>)
     * @author Andrew.Dong
     */
    private static void convertKeys(Object obj, Map<String, String> keyMapping) {

        if (null == obj || CollectionUtils.isEmpty(keyMapping)) {
            return;
        }

        for (Entry<String, String> entry : keyMapping.entrySet()) {
            convertKeys(obj, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 转换JSON对象中的键值名称
     *
     * @param obj    JSON对象(Map or List)
     * @param oldKey 原来的键值名称(Ex: data.productCode)
     * @param newKey 新的键值名称(Ex: supCode)
     * @author Andrew.Dong
     */
    @SuppressWarnings("unchecked")
    private static void convertKeys(Object obj, String oldKey, String newKey) {

        if (null == obj) {
            return;
        }

        if (obj instanceof Map) {
            convertMapKey((Map<String, Object>) obj, oldKey, newKey);
        } else if (obj instanceof List) {
            for (Object sObj : (List<Object>) obj) {
                convertKeys(sObj, oldKey, newKey);
            }
        }
    }

    /**
     * 转换JSON对象Map中的键值名称
     *
     * @param map    JSON对象(Map)
     * @param oldKey 原来的键值名称(Ex: data.productCode)
     * @param newKey 新的键值名称(Ex: supCode)
     * @author Andrew.Dong
     */
    private static void convertMapKey(Map<String, Object> map, String oldKey, String newKey) {

        int firstPoint = oldKey.indexOf('.');
        if (firstPoint < 0) {
            MapUtils.convertKey(map, oldKey, newKey);
            return;
        }

        String left = oldKey.substring(0, firstPoint);
        String right = oldKey.substring(firstPoint + 1);

        convertKeys(map.get(left), right, newKey);
    }

    /**
     * 对象复制
     *
     * @param source
     * @param target
     */
    public static void copyProps(Object source, Object target) {

        copyProps(source, target, null);
    }

    /**
     * 对象复制
     *
     * @param source
     * @param target
     * @param igonres
     */
    public static void copyProps(Object source, Object target, String[] igonres) {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        // 需复制字段
        for (PropertyDescriptor targetPd : targetPds) {

            if (isIgonre(targetPd, igonres)) {
                continue;
            }

            copyProp(source, target, targetPd);
        }
    }

    // 判断是否是需要忽略的字段
    private static boolean isIgonre(PropertyDescriptor targetPd, String[] igonres) {

        if (targetPd.getWriteMethod() == null) {
            return true;
        }

        if (null == igonres) {
            return false;
        }

        // 忽略字段
        for (int i = 0; i < igonres.length; i++) {
            if (igonres[i] == null) {// 判空
                continue;
            }

            if (targetPd.getName().equals(igonres[i])) {// 是否为忽略字段
                LOGGER.info(targetPd.getName());
                return true;
            }
        }

        return false;
    }

    // 复制单个字段的值
    private static void copyProp(Object source, Object target, PropertyDescriptor targetPd) {

        Object methodName = null;

        try {
            PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
            if (null == sourcePd || null == sourcePd.getReadMethod()) { // NOSONAR
                return;
            }

            Method readMethod = sourcePd.getReadMethod();
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            Object value = readMethod.invoke(source);
            if (null == value) {
                return;
            }

            Method writeMethod = targetPd.getWriteMethod();
            methodName = writeMethod.getName();
            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            writeMethod.invoke(target, value);
        } catch (BeansException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            String errMsg = String.format(EXCEPTION_LOG_FORMAT_COPYPROPS, methodName, e.getMessage());
            LOGGER.error(errMsg);
            throw new ResponderException(errMsg);
        }
    }

}
