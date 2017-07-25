package io.intino.konos.server.activity.dialogs;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Form {
    String context = null;
    Map<String, Input> inputsMap = new HashMap<>();

    private static final String AlphaAndDigits = "[^a-zA-Z0-9]+";

    private Form(String context, Map<String, Input> inputsMap) {
        this.inputsMap = inputsMap;
    }

    public String context() {
        return context;
    }

    public Input input(String name) {
        name = normalizeName(name);
        return inputsMap.containsKey(name) ? inputsMap.get(name) : null;
    }

    public static Form fromJson(String json) {
        return new Gson().fromJson(json, Form.class);
    }

    public static Form fromMap(String context, Map<String, Input> inputMap) {
        return new Form(context, inputMap);
    }

    public static class Input {
        private String type;
        private List<Object> values;

        public Input(String type, List<Object> values) {
            this.type = type;
            this.values = values;
        }

        public String type() {
            return type;
        }

        public Value value() {
            Object value = values.size() > 0 ? values.get(0) : null;
            if (value == null) return null;

            return new Value() {
                @Override
                public String asString() {
                    return (String)value;
                }

                @Override
                public boolean asBoolean() {
                    return Boolean.valueOf((String) value);
                }

                @Override
                public int asInteger() {
                    return Integer.valueOf((String)value);
                }

                @Override
                public double asDouble() {
                    return Double.valueOf((String)value);
                }

                @Override
                public InputStream asResource() {
                    return new ByteArrayInputStream(Base64.decodeBase64((String)value));
                }
            };
        }

        public Values values() {
            return new Values() {
                @Override
                public List<String> asString() {
                    return values.stream().map(v -> (String)v).collect(toList());
                }

                @Override
                public List<Boolean> asBoolean() {
                    return values.stream().map(v -> Boolean.valueOf((String)v)).collect(toList());
                }

                @Override
                public List<Integer> asInteger() {
                    return values.stream().map(v -> Integer.valueOf((String)v)).collect(toList());
                }

                @Override
                public List<Double> asDouble() {
                    return values.stream().map(v -> Double.valueOf((String)v)).collect(toList());
                }

                @Override
                public List<InputStream> asResource() {
                    return values.stream().map(v -> new ByteArrayInputStream(Base64.decodeBase64((String)v))).collect(toList());
                }
            };
        }

        public interface Value {
            String asString();
            boolean asBoolean();
            int asInteger();
            double asDouble();
            InputStream asResource();
        }

        public interface Values {
            List<String> asString();
            List<Boolean> asBoolean();
            List<Integer> asInteger();
            List<Double> asDouble();
            List<InputStream> asResource();
        }
    }

    private String normalizeName(String name) {
        return name.replaceAll(AlphaAndDigits,"");
    }

}
