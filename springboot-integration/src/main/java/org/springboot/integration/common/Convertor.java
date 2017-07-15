package org.springboot.integration.common;


import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Convertor<C> {
    private Class<C> constructKlass;

    public Convertor(Class<C> constructKlass) {
        this.constructKlass = Objects.requireNonNull(constructKlass, "转换对象的构造类型不能为空");
    }


    public static <C> Convertor<C> of(Class<C> constructKlass) {
        return new Convertor<C>(constructKlass);
    }


    public C from(Object source, String... filedNamesToSkip) {
        if (source == null) {
            return null;
        }
        try {
            C result = constructKlass.newInstance();
            BeanUtils.copyProperties(source, result, filedNamesToSkip);
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public List<C> createFrom(Iterable<?> sources, String... filedNamesToSkip) {
        if (sources == null) {
            return null;
        }
        List<C> result = new ArrayList<C>();
        for (Object source : sources) {
            result.add(from(source, filedNamesToSkip));
        }
        return result;
    }
}