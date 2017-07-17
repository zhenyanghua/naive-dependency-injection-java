package io.hua;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Container
 */
public class Container {
    private static Container context;
    private Map<Class<?>, ClassDetail> classes = new HashMap<>();
    private Map<Class<?>, Object> instances = new HashMap<>();

    private Container(final ClassDetail[] details) throws ClassNotFoundException {
        for (ClassDetail detail: details) {
            classes.put(Class.forName(detail.getClassName()), detail);
            instances.put(Class.forName(detail.getClassName()), null);
        }
    }

    /**
     * getContext
     * @param details
     * @return
     * @throws ClassNotFoundException
     * @apiNote Factory method to return the single context object.
     */
    public static Container getContext(final ClassDetail[] details) throws ClassNotFoundException {
        if (context == null) {
            context = new Container(details);
            System.out.println("This creates a context singleton.");
        }
        return context;
    }

    /**
     * getInstance
     * @param clazz
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @apiNote Uses reflection to create class instances
     */
    public <T> T getInstance(final Class<T> clazz) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!classes.containsKey(clazz))
            throw new ClassNotFoundException();

        if (classes.get(clazz).getScope() == Scope.Prototype)
            return newInstance(clazz);

        if (instances.get(clazz) == null) {
            T instance = newInstance(clazz);
            instances.put(clazz, instance);
        }

        return clazz.cast(instances.get(clazz));
    }

    private <T> T newInstance(final Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String[] dependentClassNames = classes.get(clazz).getDependencies();
        Object[] dependentInstances = new Object[dependentClassNames.length];
        Class<?>[] dependentClasses = new Class[dependentClassNames.length];

        for(int i = 0; i < dependentInstances.length; i++) {
            final String dependentClassName = dependentClassNames[i];

            dependentClasses[i] = Class.forName(dependentClassName);
            dependentInstances[i] = getInstance(dependentClasses[i]);
        }

        Constructor<T> ctr = clazz.getConstructor(dependentClasses);
        return ctr.newInstance(dependentInstances);
    }
}
