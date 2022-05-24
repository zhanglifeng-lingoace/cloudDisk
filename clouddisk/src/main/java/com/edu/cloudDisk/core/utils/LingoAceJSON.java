package com.edu.cloudDisk.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.edu.cloudDisk.core.log.LogPrint;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LingoAceJSON {
    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public Object findSerializer(Annotated a) {
                if ((a instanceof AnnotatedMethod)) {
                    AnnotatedElement m = a.getAnnotated();
                    DateTimeFormat an = (DateTimeFormat) m.getAnnotation(DateTimeFormat.class);
                    if ((an != null) &&
                            (!"yyyy-MM-dd HH:mm:ss".equals(an.pattern()))) {
                        return new JsonDateSerializer(an.pattern());
                    }
                }
                return super.findSerializer(a);
            }
        });
    }

    public static String Serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            LogPrint.error("LingoAceJSON.Serialize(Object obj)", e);
            return null;
        }
    }

    public static String Serialize(Object obj, SerializationFeature feature) {
        if (obj == null) {
            return null;
        }
        try {
            objectMapper.configure(feature, true);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            LogPrint.error("LingoAceJSON.Serialize(Object obj, SerializationFeature feature)", e);
            return null;
        }
    }

    public static <T> T DeSerialize(String str, Class<T> cls) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return objectMapper.readValue(str, cls);
        } catch (Exception e) {
            LogPrint.error("LingoAceJSON.DeSerialize(String str, Class<T> cls)", e);
            return null;
        }
    }

    public static <T> T DeSerialize(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (Exception e) {
            LogPrint.error("LingoAceJSON.DeSerialize(String str, TypeReference<T> typeReference)", e);
            return null;
        }
    }

    public static <T> T DeSerialize(String str, Class<T> cls, DeserializationFeature feature) {
        objectMapper.configure(feature, true);
        return DeSerialize(str, cls);
    }


    /**
     * 解决JSON与对象时间转换问题
     */
    private static class JsonDateSerializer extends JsonSerializer<Date> {
        private SimpleDateFormat dateFormat;

        public JsonDateSerializer(String format) {
            this.dateFormat = new SimpleDateFormat(format);
        }

        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            String value = this.dateFormat.format(date);
            gen.writeString(value);
        }
    }
}
