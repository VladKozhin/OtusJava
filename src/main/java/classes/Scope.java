package classes;// Scope.java
import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Object> dependencies = new HashMap<>();
    private Object lastResult;

    public void register(String key, Object value) {
        dependencies.put(key, value);
    }

    public boolean tryGet(String key, java.util.function.Consumer<Object> consumer) {
        if (dependencies.containsKey(key)) {
            consumer.accept(dependencies.get(key));
            return true;
        }
        return false;
    }

    public void setLastResult(Object result) {
        this.lastResult = result;
    }

    public Object getLastResult() {
        return lastResult;
    }
}