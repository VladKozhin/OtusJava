package classes;

import interfaces.ICommand;

import java.lang.reflect.*;

public class AdapterFactory {

    public static <T> T createAdapter(Class<T> iface, Object obj) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();

                if (methodName.equals("getPosition")) {
                    return IoC.resolve(Vector.class.toString(), "Spaceship.Operations.IMovable:position.get", obj);
                } else if (methodName.equals("getVelocity")) {
                    return IoC.resolve(Vector.class.toString(), "Spaceship.Operations.IMovable:velocity.get", obj);
                } else if (methodName.equals("setPosition")) {
                    // Предполагается, что IoC.resolve возвращает ICommand с методом execute()
                    ICommand command = IoC.resolve(ICommand.class.toString(), "Spaceship.Operations.IMovable:position.set", obj, args[0]);
                    command.execute();
                    return null;
                }

                throw new UnsupportedOperationException("Method not supported: " + methodName);
            }
        };

        Object proxyInstance = Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                handler);

        return iface.cast(proxyInstance);
    }
}
