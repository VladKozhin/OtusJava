package interfaces;

import java.util.Map;

public interface IAbstractObject {
    Object getField(String fieldName);
    void setField(String key, Object value);
    Map<String, Object> getParams();
}