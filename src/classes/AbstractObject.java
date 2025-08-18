package classes;

import interfaces.IAbstractObject;

import java.util.HashMap;
import java.util.Map;

public class AbstractObject implements IAbstractObject {
    private Map<String, Object> params;

    public AbstractObject(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Object getField(String fieldName) {
        if (!params.containsKey(fieldName)) {
            throw new RuntimeException("Field " + fieldName + " can't be read!");
        }
        return params.get(fieldName);
    }

    @Override
    public void setField(String key, Object value) {
        params.put(key, value);
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }
}