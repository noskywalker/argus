package com.monitor.argus.common.util;

import org.apache.commons.beanutils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Bean工具类
 * 
 * @Author null
 * @Date 2014-4-28 下午06:22:54
 * 
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private static BeanUtilsBean utilsBean;

    static {
        ConvertUtilsBean convertUtils = new ConvertUtilsBean();
        convertUtils.register(new DateConverter(), Date.class);
        utilsBean = new BeanUtilsBean(convertUtils, new PropertyUtilsBean());
    }

    /**
     * 拷贝属性，排除Null值
     * 
     * @param target
     * @param source
     */
    public synchronized static void copyProperties(Object target, Object source) {

        if (target == null || source == null)
            return;

        // 获取属性描述
        PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(source.getClass());
        // 遍历属性
        for (PropertyDescriptor descriptor : properties) {
            String propName = descriptor.getName();
            try {
                // 如果不为空，则复制
                Object obj = PropertyUtils.getProperty(source, propName);
                if (obj != null && descriptor.getWriteMethod() != null)
                    utilsBean.setProperty(target, propName, obj);

            } catch (Exception e) {
                logger.error(e.toString());
                logger.error("property name=" + propName);
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝属性，排除Null值，合并mergeList中的属性值(String类型属性)
     * 
     * @param target
     * @param source
     * @param mergePropList
     */
    public synchronized static void copyProperties(Object target, Object source, List<String> mergePropList) {
        if (target == null || source == null)
            return;

        if (mergePropList == null) {
            copyProperties(target, source);
            return;
        }

        // 获取属性描述
        PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(source.getClass());
        // 遍历属性
        for (PropertyDescriptor descriptor : properties) {
            String propName = descriptor.getName();
            try {
                // 如果不为空，则复制
                Object objSrc = PropertyUtils.getProperty(source, propName);
                if (objSrc != null && descriptor.getWriteMethod() != null) {
                    Object objTar = PropertyUtils.getProperty(target, propName);
                    if (objTar != null && (objTar instanceof String) && mergePropList.contains(propName)) {
                        // 合并属性
                        utilsBean.setProperty(target, propName, (objTar + " <br> " + objSrc));
                    } else
                        utilsBean.setProperty(target, propName, objSrc);
                }
            } catch (Exception e) {
                logger.error(e.toString());
                logger.error("property name=" + propName);
                e.printStackTrace();
            }
        }
    }

    /**
     * 对比bean之间属性值，返回不一致属性名的列表 TODO
     * 
     * @author alex zhang
     * @author
     * @CreateDate 2015年12月3日 下午5:55:11
     * 
     * @param object1
     * @param object2
     * @return
     */
    public synchronized static List<String> getDifferentValueProperties(Object object1, Object object2) {
        if (object1 == null || object2 == null)
            return null;

        List<String> difPropList = new ArrayList<String>();
        // 获取object1属性描述
        PropertyDescriptor[] properties1 = PropertyUtils.getPropertyDescriptors(object1.getClass());
        // 获取object2属性描述
        PropertyDescriptor[] properties2 = PropertyUtils.getPropertyDescriptors(object2.getClass());

        for (PropertyDescriptor descriptor1 : properties1) {
            String propName1 = descriptor1.getName();
            if (propName1 != null) {
                try {
                    Object propValue1 = PropertyUtils.getProperty(object1, propName1);
                    for (PropertyDescriptor descriptor2 : properties2) {
                        String propName2 = descriptor2.getName();
                        if (propName2 != null && propName2.equals(propName1)) {
                            Object propValue2 = PropertyUtils.getProperty(object2, propName2);
                            if (propValue1 == null && propValue2 == null) {
                                break;
                            } else if ((propValue1 == null && propValue2 != null)
                                    || (propValue1 != null && propValue2 == null)) {
                                difPropList.add(propName1);
                                break;
                            } else if (!propValue1.equals(propValue2)) {
                                difPropList.add(propName1);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("get Different Value Properties - {}", e);
                    logger.error("get Different Value Properties - property name={}", propName1);
                }
            }
        }
        return difPropList;
    }

    /**
     * 对比bean之间属性值，返回不一致属性名的列表,对比mergeList中的属性值(String类型属性) TODO
     * 
     * @param object1
     * @param object2
     * @param comparePropList
     */
    public synchronized static List<String> getDifferentValueProperties(Object object1, Object object2,
                                                                        List<String> comparePropList) {
        if (object1 == null || object2 == null)
            return null;

        if (comparePropList == null) {
            return getDifferentValueProperties(object1, object2);
        }

        List<String> difPropList = new ArrayList<String>();

        // 获取object1属性描述
        PropertyDescriptor[] properties1 = PropertyUtils.getPropertyDescriptors(object1.getClass());
        // 获取object2属性描述
        PropertyDescriptor[] properties2 = PropertyUtils.getPropertyDescriptors(object2.getClass());

        for (PropertyDescriptor descriptor1 : properties1) {
            String propName1 = descriptor1.getName();
            if (propName1 != null && comparePropList.contains(propName1)) {
                try {
                    Object propValue1 = PropertyUtils.getProperty(object1, propName1);
                    for (PropertyDescriptor descriptor2 : properties2) {
                        String propName2 = descriptor2.getName();
                        if (propName2 != null && propName2.equals(propName1)) {
                            Object propValue2 = PropertyUtils.getProperty(object2, propName2);
                            if (propValue1 == null && propValue2 == null) {
                                break;
                            } else if ((propValue1 == null && propValue2 != null)
                                    || (propValue1 != null && propValue2 == null)) {
                                difPropList.add(propName1);
                                break;
                            } else if (!propValue1.equals(propValue2)) {
                                difPropList.add(propName1);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("get Different Value Properties - {}", e);
                    logger.error("get Different Value Properties - property name={}", propName1);
                }
            }
        }
        return difPropList;
    }

    /**
     * 复制最新属性值
     * 
     * @author alex zhang
     * @author
     * @CreateDate 2015年12月3日 下午5:55:11
     * 
     * @param targetParameter
     * @param srcParameter
     * @return
     */
    public synchronized static Object getNewestPropertiesBean(Object newTargetObject, Object targetParameter,
                                                              Object srcParameter) {
        if (srcParameter == null || targetParameter == null)
            return null;
        Object newTarBean = newTargetObject;

        List<String> difPropList = new ArrayList<String>();
        // 获取target属性描述
        PropertyDescriptor[] sProperties = PropertyUtils.getPropertyDescriptors(srcParameter.getClass());
        // 获取src属性描述
        PropertyDescriptor[] tProperties = PropertyUtils.getPropertyDescriptors(targetParameter.getClass());

        for (PropertyDescriptor sDescriptor : sProperties) {
            String sPropName = sDescriptor.getName();
            if (sPropName != null) {
                try {
                    Object sPropValue = PropertyUtils.getProperty(srcParameter, sPropName);
                    for (PropertyDescriptor tDescriptor : tProperties) {
                        String tPropName = tDescriptor.getName();
                        if (tPropName != null && tPropName.equals(sPropName)) {
                            Object tPropValue = PropertyUtils.getProperty(targetParameter, tPropName);
                            if (sPropValue == null) {
                                break;
                            } else if (tPropValue == null && sPropValue != null) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    utilsBean.setProperty(newTarBean, tPropName, sPropValue);
                                    difPropList.add(sPropName);
                                }
                                break;
                            } else if (!tPropValue.equals(sPropValue)) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    utilsBean.setProperty(newTarBean, tPropName, sPropValue);
                                    difPropList.add(sPropName);
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("getNewestPropertiesBean - {}", e);
                    logger.error("getNewestPropertiesBean - property name={}", sPropName);
                }
            }
        }

        if (difPropList != null && difPropList.size() > 0) {
            return newTarBean;
        } else {
            return null;
        }
    }

    /**
     * 复制mergeList中的属性最新值
     * 
     * @param srcParameter
     * @param targetParameter
     * @param comparePropList
     */
    public synchronized static Object getNewestPropertiesBean(Object newTargetObject, Object targetParameter,
                                                              Object srcParameter, List<String> comparePropList) {
        if (srcParameter == null || targetParameter == null)
            return null;

        if (comparePropList == null) {
            return getNewestPropertiesBean(newTargetObject, srcParameter, targetParameter);
        }
        Object newTarBean = newTargetObject;

        List<String> difPropList = new ArrayList<String>();

        // 获取target属性描述
        PropertyDescriptor[] tProperties = PropertyUtils.getPropertyDescriptors(targetParameter.getClass());
        // 获取src属性描述
        PropertyDescriptor[] sProperties = PropertyUtils.getPropertyDescriptors(srcParameter.getClass());

        for (PropertyDescriptor sDescriptor : sProperties) {
            String sPropName = sDescriptor.getName();
            if (sPropName != null && comparePropList.contains(sPropName)) {
                try {
                    Object sPropValue = PropertyUtils.getProperty(srcParameter, sPropName);
                    for (PropertyDescriptor tDescriptor : tProperties) {
                        String tPropName = tDescriptor.getName();
                        if (tPropName != null && tPropName.equals(sPropName)) {
                            Object tPropValue = PropertyUtils.getProperty(targetParameter, tPropName);
                            if (sPropValue == null) {
                                break;
                            } else if (tPropValue == null && sPropValue != null) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    utilsBean.setProperty(newTarBean, tPropName, sPropValue);
                                    difPropList.add(sPropName);
                                }
                                break;
                            } else if (!tPropValue.equals(sPropValue)) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    utilsBean.setProperty(newTarBean, tPropName, sPropValue);
                                    difPropList.add(sPropName);
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("getNewestPropertiesBean - {}", e);
                    logger.error("getNewestPropertiesBean - property name={}", sPropName);
                }
            }
        }

        if (difPropList != null && difPropList.size() > 0) {
            return newTarBean;
        } else {
            return null;
        }
    }

    /**
     * 比较现在的对象是否对老对象有更新
     * 
     * @author null
     * @date 2015年12月10日 下午9:22:28
     * 
     * @param newTargetObject
     * @param oldParameter
     * @param nowParameter
     * @return
     */
    public synchronized static Boolean isDifferentPropertiesFromSrcBean(Object oldParameter, Object nowParameter) {
        if (nowParameter == null || oldParameter == null)
            return null;
        // 获取target属性描述
        PropertyDescriptor[] sProperties = PropertyUtils.getPropertyDescriptors(nowParameter.getClass());
        // 获取src属性描述
        PropertyDescriptor[] tProperties = PropertyUtils.getPropertyDescriptors(oldParameter.getClass());

        for (PropertyDescriptor sDescriptor : sProperties) {
            String sPropName = sDescriptor.getName();
            if (sPropName != null) {
                try {
                    Object sPropValue = PropertyUtils.getProperty(nowParameter, sPropName);
                    for (PropertyDescriptor tDescriptor : tProperties) {
                        String tPropName = tDescriptor.getName();
                        if (tPropName != null && tPropName.equals(sPropName)) {
                            Object tPropValue = PropertyUtils.getProperty(oldParameter, tPropName);
                            if (sPropValue == null) {
                                break;
                            } else if (tPropValue == null && sPropValue != null) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    return true;
                                }
                                break;
                            } else if (!tPropValue.equals(sPropValue)) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    return true;
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("isChangedPropertiesBean - {}", e);
                    logger.error("isChangedPropertiesBean - property name={}", sPropName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 比较comparePropList属性中，现在的对象是否对老对象有更新
     * 
     * @param nowParameter
     * @param oldParameter
     * @param comparePropList
     */
    public synchronized static Boolean isChangedPropertiesBean(Object oldParameter, Object nowParameter,
                                                               List<String> comparePropList) {
        if (nowParameter == null || oldParameter == null)
            return null;

        if (comparePropList == null) {
            return isDifferentPropertiesFromSrcBean(nowParameter, oldParameter);
        }
        // 获取target属性描述
        PropertyDescriptor[] tProperties = PropertyUtils.getPropertyDescriptors(oldParameter.getClass());
        // 获取src属性描述
        PropertyDescriptor[] sProperties = PropertyUtils.getPropertyDescriptors(nowParameter.getClass());

        for (PropertyDescriptor sDescriptor : sProperties) {
            String sPropName = sDescriptor.getName();
            if (sPropName != null && comparePropList.contains(sPropName)) {
                try {
                    Object sPropValue = PropertyUtils.getProperty(nowParameter, sPropName);
                    for (PropertyDescriptor tDescriptor : tProperties) {
                        String tPropName = tDescriptor.getName();
                        if (tPropName != null && tPropName.equals(sPropName)) {
                            Object tPropValue = PropertyUtils.getProperty(oldParameter, tPropName);
                            if (sPropValue == null) {
                                break;
                            } else if (tPropValue == null && sPropValue != null) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    return true;
                                }
                                break;
                            } else if (!tPropValue.equals(sPropValue)) {
                                if (tDescriptor.getWriteMethod() != null) {
                                    return true;
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("isChangedPropertiesBean - {}", e);
                    logger.error("isChangedPropertiesBean - property name={}", sPropName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * @param target
     * @param property
     */
    public synchronized static String getStringProperty(Object target, String property) throws Exception {
        if (target == null)
            return null;
        return BeanUtils.getProperty(target, property);
    }

    /**
     * @param target
     * @param property
     */
    public synchronized static Object getProperty(Object target, String property) throws Exception {
        if (target == null)
            return null;
        return PropertyUtils.getProperty(target, property);
    }

    /**
     * 设置属性
     * 
     * @param obj
     * @param propertyName
     * @param value
     * @throws Exception
     */
    public synchronized static void setProperty(Object obj, String propertyName, Object value) throws Exception {
        if (obj == null)
            return;
        if (obj instanceof HashMap) {
            HashMap map = (HashMap) obj;
            setProperty(map, propertyName, value);
            return;
        }

        String pros[] = StringUtil.split(propertyName, ".");
        if (propertyName == null || propertyName.trim().equals(""))
            return;
        Object parentObj = obj;
        // 多级对象属性设置
        if (pros.length > 1) {
            for (int k = 0; k < pros.length - 1; k++) {
                Object subObj = PropertyUtils.getProperty(parentObj, pros[k]);
                if (subObj == null) {
                    PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(parentObj, pros[k]);
                    subObj = pd.getPropertyType().newInstance();
                    BeanUtils.setProperty(parentObj, pros[k], subObj);
                }
                parentObj = subObj;
            }
        }
        utilsBean.setProperty(obj, propertyName, value);
    }

    private synchronized static void setProperty(HashMap map, String propertyName, Object value) throws Exception {
        if (propertyName == null || propertyName.trim().equals(""))
            return;
        String pros[] = StringUtil.split(propertyName, ".");
        // 多级对象属性设置
        HashMap parentMap = map;
        if (pros.length > 1) {
            for (int k = 0; k < pros.length; k++) {
                if (k == pros.length - 1) {
                    parentMap.put(pros[k], value);
                    continue;
                }
                Object subMap = map.get(pros[k]);
                if (subMap == null) {
                    subMap = new HashMap();
                    parentMap.put(pros[k], subMap);
                }
                parentMap = (HashMap) subMap;
            }
        } else {
            map.put(propertyName, value);
        }
    }

    public static <T> T getParamByBean(HttpServletRequest request, Class<T> clazz) throws Exception {
        T bean = null;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException e) {
            logger.debug(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.debug(e.getMessage());
        }

        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            String val = request.getParameter(key);
            if (!val.isEmpty())
                BeanUtils.setProperty(bean, key, val);
        }

        return bean;
    }
}
