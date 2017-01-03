package com.tt.findyou.utils;

import java.util.Collection;

/**
 * Created by TT on 2016/12/7.
 */
public class EmptyUtil {
    public static boolean isEmpty(Object object){
        if(object == null)
            return true;
        if(object instanceof String)
            return ((String) object).trim().isEmpty();
        if(object instanceof Collection)
            return ((Collection) object).isEmpty();
        if(object instanceof Object[])
            return ((Object[]) object).length==0;
        if(object instanceof int[])
            return ((int[]) object).length==0;
        if(object instanceof double[])
            return ((double[]) object).length==0;
        if(object instanceof float[])
            return ((float[]) object).length==0;
        if(object instanceof byte[])
            return ((byte[]) object).length==0;
        if(object instanceof long[])
            return ((long[]) object).length==0;
        if(object instanceof short[])
            return ((short[]) object).length==0;
        return false;
    }
    public static boolean isNotEmpty(Object object){
        return !isEmpty(object);
    }
}
