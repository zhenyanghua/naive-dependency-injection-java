package io.hua;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Container
 */
public class Container {
    private static Container context;
    private Map<Class<?>, ClassDetail> classes = new HashMap<>();
    private Map<Class<?>, Object> instances = new HashMap<>();
    private Map<Class<?>, Set<Class<?>>> classInterfaces = new HashMap<>();

    private Container(final ClassDetail[] details) throws ClassNotFoundException {
        for (ClassDetail detail: details) {
            Class<?> clazz = Class.forName(detail.getClassName());
            classes.put(clazz, detail);
            instances.put(clazz, null);
            classInterfaces.put(clazz, new HashSet<>(Arrays.asList(clazz.getInterfaces())));
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

        Class<?> implClass = getImplementationClass(clazz);
        if (classes.get(implClass).getScope() == Scope.Prototype)
            return newInstance(implClass);

        if (instances.get(implClass) == null) {
            T instance = newInstance(implClass);
            instances.put(implClass, instance);
        }

        return clazz.cast(instances.get(implClass));
    }

    private <T> T newInstance(final Class<?> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String[] dependentClassNames = classes.get(clazz).getDependencies();
        Object[] dependentInstances = new Object[dependentClassNames.length];
        Class<?>[] dependentClasses = new Class[dependentClassNames.length];

        for(int i = 0; i < dependentInstances.length; i++) {
            final String dependentClassName = dependentClassNames[i];
            final Class<?> dependentClass = Class.forName(dependentClassName);
            final Class<?>[] interfaces = dependentClass.getInterfaces();
            String[] interfaceNames = Arrays.stream(interfaces).map(Class::getName).toArray(String[]::new);

            dependentClasses[i] = Class.forName(guessClassInterface(dependentClassName, interfaceNames));
            dependentInstances[i] = getInstance(dependentClasses[i]);
        }

        Constructor<?> ctr = clazz.getConstructor(dependentClasses);
        return (T) ctr.newInstance(dependentInstances);
    }

    private <T> Class<?> getImplementationClass(final Class<T> clazz) throws ClassNotFoundException {
        for (Map.Entry<Class<?>, Set<Class<?>>> entry:classInterfaces.entrySet()) {
            if (entry.getValue().contains(clazz)) {
                return entry.getKey();
            }
        }

        if (!classes.containsKey(clazz))
            throw new ClassNotFoundException();

        return clazz;
    }

    private String guessClassInterface(String implClassName, String[] interfaceNames) {
        final String simpleImplClassName = getSimpleClassNameFromString(implClassName)
            .toLowerCase().replace("impl", "");
        for (String interfaceName: interfaceNames) {
            if (getSimpleClassNameFromString(interfaceName).toLowerCase().contains(simpleImplClassName))
                return interfaceName;
        }
        return implClassName;
    }

    private String getSimpleClassNameFromString(String className) {
        final String[] classNameArray = className.split("\\.");
        return classNameArray[classNameArray.length - 1];
    }
}
