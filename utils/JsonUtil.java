package com.sheca.unitrust.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.sheca.unitrust.framework.exception.FrameworkException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * JsonUtil
 *
 * @author psn
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class JsonUtil {

    private final static ObjectMapper objectMapper = initObjectMapper(new ObjectMapper());

    /**
     * initial objectMapper
     *
     * @param objectMapper
     * @return
     */
    public static ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
        }
        return doInitObjectMapper(objectMapper);
    }

    /**
     * format objectMapper time
     *
     * @param objectMapper
     * @return
     */
    private static ObjectMapper doInitObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //不显示为null的字段
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 忽略不能转移的字符
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        // 过滤对象的null属性.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略transient
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    /**
     * getter objectMapper
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * object2json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (isCharSequence(object)) {
            return (String) object;
        }
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new FrameworkException(e);
        }
    }

    /**
     * json2object
     *
     * @param json
     * @return
     */
    public static Object parse(String json) {
        Object object = null;
        try {
            object = getObjectMapper().readValue(json, Object.class);
        } catch (Exception ignored) {
        }
        return object;
    }

    /**
     * is charsequence
     *
     * @param object
     * @return
     */
    public static Boolean isCharSequence(Object object) {
        return !Objects.isNull(object) && StringUtils.isCharSequence(object.getClass());
    }

    /**
     * wapper objectMapper
     *
     * @return
     */
    public static Consumer<HttpMessageConverter<?>> wrapperObjectMapper() {
        return converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter httpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                initObjectMapper(httpMessageConverter.getObjectMapper());
            }
        };
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        T t = null;
        try {
            t = getObjectMapper().readValue(json, clazz);
        } catch (Exception ignored) {
        }
        return t;
    }

    public static <T> T readValue(String json, TypeReference valueTypeRef) {
        T t = null;
        try {
            t = getObjectMapper().readValue(json, valueTypeRef);
        } catch (Exception ignored) {
        }
        return t;
    }
}
