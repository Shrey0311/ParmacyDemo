package com.ssp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HBGNUtil {

    public HashMap<String, Object> newKeyValueMap(String keys, Object... values) {
        HashMap<String, Object> newMap = new HashMap<>();
        this.fillWithArray(newMap, keys, values);
        return newMap;
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    private void fillWithArray(final Map _newMap, final String _keys, final Object... _values) {
        if (Objects.isNull(_values)) {
            return;
        }
        String[] keys = _keys.split("_");
        int count = keys.length;
        if (_values.length < count) {
            count = _values.length;
        }
        for (int i = 0; i < count; i++) {
            _newMap.put(keys[i], _values[i]);
        }
    }
}
