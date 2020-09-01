package com.upgrade.common.util;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class ConversionUtil {

    @Autowired
    private ConversionService conversionService;

    public <S, T> List<T> convert(List<S> source, Class<T> targetClass) {
        return CollectionUtils.emptyIfNull(source).stream().map(s ->
                conversionService.convert(s, targetClass))
                .collect(Collectors.toList());
    }
}
