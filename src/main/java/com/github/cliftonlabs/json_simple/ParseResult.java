package com.github.cliftonlabs.json_simple;

import java.math.BigDecimal;

/**
 * Wrapped deserialize result
 * Used to fastly navigate through resulted JSON
 * @author <a href="https://github.com/SashaSemenishchev">SashaSemenishchev</a>
 */
public class ParseResult implements Accessor {
    private final Jsonable result;

    public ParseResult(Jsonable result) {
        this.result = result;
    }

    /**
     *
     * @param index The index of resulted array to get
     * @return Accessor to manipulate output or continue digging into json further
     */
    public ParseResultAccessor get(int index) {
        return new ParseResultAccessor(result, index);
    }

    /**
     * @param key The key of the object
     * @return Accessor to manipulate output or continue digging into json further
     */
    public ParseResultAccessor get(String key) {
        return new ParseResultAccessor(result, key);
    }

    @Override
    public boolean isArray() {
        return result instanceof JsonArray;
    }

    @Override
    public boolean isJsonObject() {
        return result instanceof JsonObject;
    }

    @Override
    public boolean isNull() {
        return result == null;
    }

    @Override
    public JsonObject asJsonObject() {
        return (JsonObject) result;
    }

    @Override
    public JsonArray asArray() {
        return (JsonArray) result;
    }

    public static class ParseResultAccessor implements Accessor {
        private Object init;

        public ParseResultAccessor(Jsonable init, int first) {
            this.init = init;
            get(first);
        }

        public ParseResultAccessor(Jsonable init, String first) {
            this.init = init;
            get(first);
        }

        /**
         * Used if current element is {@link JsonArray} and it's needed to continue by index
         * @param index Index in the array
         * @return Array element accessor
         */
        public ParseResultAccessor get(int index) {
            this.init = ((JsonArray)this.init).get(index);
            return this;
        }

        /**
         * Used if current element is {@link JsonObject} and it's needed to continue by string key
         * @param key Key in the object
         * @return Object element accessor
         */
        public ParseResultAccessor get(String key) {
            this.init = ((JsonObject)this.init).get(key);
            return this;
        }

        /**
         * Used if it's needed to save current JSON position in the variable, for example to reduce amount of code
         * when needed objects are in the same root object
         * @return Parse result on the current point of the accessor
         */
        public ParseResult fixate() {
            return new ParseResult((Jsonable) init);
        }

        // Methods used to convert current position to needed objects

        public int asInt() {
            return asNumber().intValue();
        }

        public double asDouble() {
            return asNumber().doubleValue();
        }

        public float asFloat() {
            return asNumber().floatValue();
        }

        public long asLong() {
            return asNumber().longValue();
        }

        public BigDecimal asNumber() {
            return (BigDecimal) init;
        }

        public boolean asBoolean() {
            try {
                return (boolean) init;
            } catch (ClassCastException exception) {
                String val = init.toString();
                if(val.equalsIgnoreCase("true")) {
                    return true;
                } else if(val.equals("false")) {
                    return false;
                } else {
                    throw new ClassCastException("Invalid boolean literal at " + ((Jsonable) init).toJson());
                }
            }
        }

        public String asString() {
            return init.toString();
        }

        @Override
        public JsonArray asArray() {
            return (JsonArray) init;
        }

        @Override
        public JsonObject asJsonObject() {
            return (JsonObject) init;
        }

        @Override
        public boolean isArray() {
            return init instanceof JsonArray;
        }

        @Override
        public boolean isJsonObject() {
            return init instanceof JsonObject;
        }

        @Override
        public boolean isNull() {
            return init == null;
        }
    }
}
