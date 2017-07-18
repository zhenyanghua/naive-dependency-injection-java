# Dependency Injection

## Popular ways to do it
- libraries that implement **JSR 330** 
> Dependency Injection for Java defines a standard set of annotations (and one interface) for use on injectable classes.

- Spring Core
  - XML style
  - Annotation style
  > works will with *JSR 330*
  - BeanPostProcessor
  > Manages lifecycle of Spring Beans

- Do It Yourself
> Helps to understand the core concept of *Dependency Injection*.

## Concept
- Application Context 
> A big container that keeps all the references to the class instances
- It should provide a way to register classes to the *Application Context*.
> So that it knows what class to instantiate from.
- It should provide a method to retrieve the *Application Context*.
> Context is just a container instance so that it get provide access to all managed instances.
- It should provide a method to retrieve instances from the *Application Context*.
> Instead of instantiate instances yourself, it will manage instances lifecycle for you.

## Do It Yourself

### 1. Create a class configuration plain old java object
```java
public class ClassDetail {
    private String className;
    private String[] dependencies;
    private Scope scope;
    
    // constructors, getters and setters omitted for brevity
    // ...
}
```

### 1. Create the container
```java
public class ApplicationContext {
    private static ApplicationContext context;
    private Map<Class<?>, ClassDetail> classes = new HashMap<>();
    private Map<Class<?>, Object> instances = new HashMap<>();
    private Map<Class<?>, Set<Class<?>>> classInterfaces = new HashMap<>();
}
```

### 2. Register the classes
```java
public class ApplicationContext {
    // ...
    private ApplicationContext(final ClassDetail[] details) throws ClassNotFoundException {
        for (final ClassDetail detail: details) {
            final Class<?> clazz = Class.forName(detail.getClassName());

            classes.put(clazz, detail);
            instances.put(clazz, null);
            classInterfaces.put(clazz, new HashSet<>(Arrays.asList(clazz.getInterfaces())));
        }
    }
}
```

### 3. Create context accessor
```java
public class ApplicationContext {
    // ...
    public static ApplicationContext getContext(final ClassDetail[] details) throws ClassNotFoundException {
        if (context == null) {
            context = new ApplicationContext(details);
            System.out.println("This creates a context singleton.");
        }
        return context;
    }
}
```

### 4. Create instance accessor
```java
public class ApplicationContext {
    // ...
    public <T> T getInstance(final Class<T> clazz) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    
        // Find and use class implementation if it has one, otherwise use the original class.
        final Class<?> implClass = getImplementationClass(clazz);
        
        // If scope is prototype, it will create a new instance every time.
        if (classes.get(implClass).getScope() == Scope.Prototype)
            return newInstance(implClass);

        // Lazy loading
        if (instances.get(implClass) == null) {
            final T instance = newInstance(implClass);
            instances.put(implClass, instance);
        }

        return clazz.cast(instances.get(implClass));
    }
    
    private <T> T newInstance(final Class<?> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Traverse dependent classes to find their dependency recursively, and use class implementation if they have one.
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
        
        // Reflectively create instances which suites all JVM environment.
        final Constructor<?> ctr = clazz.getConstructor(dependentClasses);
        return (T) ctr.newInstance(dependentInstances);
    }

    private <T> Class<?> getImplementationClass(final Class<T> clazz) throws ClassNotFoundException { /*...*/ }

    private String guessClassInterface(final String implClassName, final String[] interfaceNames) { /*...*/ }

    private String getSimpleClassNameFromString(final String className) { /*...*/ }
}
```

## Resources
1. [Spring Framework Reference Document](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/)
2. [Getting started with Spring Framework: Third Edition](https://www.amazon.com/Getting-started-Spring-Framework-Third-ebook/dp/B01HZXQFUS/ref=sr_1_1?ie=UTF8&qid=1500389139&sr=8-1&keywords=spring+framework)
3. [Introduction to Contexts and Dependency Injection for Java EE](https://docs.oracle.com/javaee/7/tutorial/cdi-basic.htm)
4. This presentation source code - [https://goo.gl/V1SVgE](https://github.com/zhenyanghua/naive-dependency-injection-java)

