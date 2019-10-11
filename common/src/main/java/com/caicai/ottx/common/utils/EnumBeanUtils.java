package com.caicai.ottx.common.utils;

import com.alibaba.citrus.service.form.Group;
import com.alibaba.citrus.service.requestcontext.support.ValueListSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Created by huaseng on 2019/9/6.
 */
public class EnumBeanUtils extends org.springframework.beans.BeanUtils {
    protected static final Logger log = LoggerFactory.getLogger(EnumBeanUtils.class);

    public static void copyProperties(Object source, Object target) throws BeansException {
        copyProperties(source, target, null, null);
    }

    private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {
        ValueListSupport valueListSupport = new ValueListSupport(new SimpleTypeConverter(), true);
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        BeanWrapper bean = new BeanWrapperImpl(target);
        BeanWrapper sourceBean = new BeanWrapperImpl(source);
        Class<?> actualEditable = source.getClass();
        PropertyDescriptor[] sourcePds = getPropertyDescriptors(actualEditable);
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            String propertyName = field.getName();
            Field field1 = null;
            try {
                field1 = target.getClass().getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (field1 == null || sourceBean.getPropertyValue(field1.getName()) == null) {
                continue;
            }
            if (bean.isWritableProperty(propertyName)) {
                try {
                    Object value = sourceBean.getPropertyValue(propertyName);
                    PropertyDescriptor pd = bean.getPropertyDescriptor(propertyName);
                    MethodParameter mp = org.springframework.beans.BeanUtils.getWriteMethodParameter(pd);
                    Object convertValue = valueListSupport.getValueOfType(pd.getPropertyType(), mp, new Object[]{value});
                    bean.setPropertyValue(propertyName, convertValue);
                } catch (ConversionNotSupportedException e) {
                    e.printStackTrace();
                }

            } else {
                log.debug("No writable property \"{}\" found in type {}", propertyName, target.getClass().getName());
            }
        }
    }
}
