package com.caicai.ottx.common.utils;

import com.caicai.ottx.common.AllowNull;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by huaseng on 2019/8/20.
 */
public class BeanUtils {
    /**
     * 获取属性类型(type)，属性名(name)，map组成的list
     *
     * @param o Object
     * @return List
     */
    public static List<Map<String, Object>> getFieldsInfo(Object o) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> infoMap;

        List<Field> fields;
        Class tmpClass = o.getClass();

        // 当父类为null的时候说明到达了最上层的父类(Object类)
        while (tmpClass != null) {
            fields = Arrays.asList(tmpClass.getDeclaredFields());
            for (Field field : fields) {
                infoMap = new HashMap<>();
                infoMap.put("type", field.getType().getName());
                infoMap.put("name", field.getName());
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(o);
                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
                }
                infoMap.put("value", value);

                // 判断是否允许值为空
                if (field.getAnnotation(AllowNull.class) != null) {
                    infoMap.put("allowNull", 1);
                } else {
                    infoMap.put("allowNull", 0);
                }
                list.add(infoMap);
            }
            tmpClass = tmpClass.getSuperclass(); //得到父类,然后赋给自己
        }

        return list;
    }

    /**
     * Map转换成相应对象
     *
     * @param map       Map
     * @param beanClass Class
     * @return Object
     * @throws Exception e
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null || map.size() <= 0) return null;

        Object obj = beanClass.newInstance();

        //获取关联的所有类，本类以及所有父类
        Class oo = obj.getClass();
        List<Class> clazzs = new ArrayList<>();
        while (true) {
            clazzs.add(oo);
            oo = oo.getSuperclass();
            if (oo == null || oo == Object.class) break;
        }

        for (Class clz : clazzs) {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                // 设置对应属性的值
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        }

        return obj;
    }

    /**
     * 对象转换成Map
     *
     * @param obj Object
     * @return Map
     * @throws Exception e
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) return null;

        // 获取关联的所有类, 本类及所有父类
        Class oo = obj.getClass();
        List<Class> clazzs = new ArrayList<>();
        while (true) {
            clazzs.add(oo);
            oo = oo.getSuperclass();
            if (oo == null || oo == Object.class) break;
        }
        Map<String, Object> map = new HashMap<>();

        for (Class clz : clazzs) {
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                int mod = field.getModifiers();

                //过滤 static 和 final 类型
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        }
        ;

        return map;
    }



}
