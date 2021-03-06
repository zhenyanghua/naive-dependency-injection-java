package io.hua.DI;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum ApplicationContext {
    CONTEXT;

    private Map<Class<?>, ClassDetail> classes = new HashMap<>();
    private Map<Class<?>, Object> instances = new HashMap<>();
    private Map<Class<?>, Set<Class<?>>> classInterfaces = new HashMap<>();

    public void init(final ClassDetail[] details) throws ClassNotFoundException {
        for (final ClassDetail detail: details) {
            final Class<?> clazz = Class.forName(detail.getClassName());

            classes.put(clazz, detail);
            classInterfaces.put(clazz, new HashSet<>(Arrays.asList(clazz.getInterfaces())));
        }
    }

    public <T> T getInstance(final Class<T> clazz) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        final Class<T> implClass = getImplementationClass(clazz);

        if (classes.get(implClass).getScope() == Scope.Prototype)
            return newInstance(implClass);

        if (!instances.containsKey(implClass)) {
            final T instance = newInstance(implClass);
            instances.put(implClass, instance);
        }

        return clazz.cast(instances.get(implClass));
    }

    private <T> T newInstance(final Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String[] dependentClassNames = classes.get(clazz).getDependencies();
        final Object[] dependentInstances = new Object[dependentClassNames.length];
        final Class<?>[] dependentClasses = new Class[dependentClassNames.length];

        for(int i = 0; i < dependentInstances.length; i++) {
            final String dependentClassName = dependentClassNames[i];
            final Class<?> dependentClass = Class.forName(dependentClassName);
            final Class<?>[] interfaces = dependentClass.getInterfaces();
            final String[] interfaceNames = Arrays.stream(interfaces).map(Class::getName).toArray(String[]::new);

            dependentClasses[i] = Class.forName(guessClassInterface(dependentClassName, interfaceNames));
            dependentInstances[i] = getInstance(dependentClasses[i]);
        }

        final Constructor<T> ctr = clazz.getConstructor(dependentClasses);

        return ctr.newInstance(dependentInstances);
    }

    // Because each entry value - interfaces set is derived from the entry key implementation Class,
    // and if the interface contains the passed class, then the implementation class is a subset
    // of the passed class type, we can safely cast it to the passed class type. Hence, suppress the
    // unchecked type cast warning.
    @SuppressWarnings("unchecked")
    private <T> Class<T> getImplementationClass(final Class<T> clazz) throws ClassNotFoundException {
        for (Map.Entry<Class<?>, Set<Class<?>>> entry:classInterfaces.entrySet()) {
            if (entry.getValue().contains(clazz)) {
                return (Class<T>) entry.getKey();
            }
        }

        if (!classes.containsKey(clazz))
            throw new ClassNotFoundException();

        return clazz;
    }

    private String guessClassInterface(final String implClassName, final String[] interfaceNames) {
        final String simpleImplClassName = getSimpleClassNameFromString(implClassName)
            .toLowerCase().replace("impl", "");

        for (final String interfaceName: interfaceNames) {
            if (simpleImplClassName.contains(getSimpleClassNameFromString(interfaceName).toLowerCase()))
                return interfaceName;
        }

        return implClassName;
    }

    private String getSimpleClassNameFromString(final String className) {
        final String[] classNameArray = className.split("\\.");

        return classNameArray[classNameArray.length - 1];
    }
}
