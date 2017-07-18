package io.hua.DI;

public class ClassDetail {
    private String className;
    private String[] dependencies;
    private Scope scope;

    public ClassDetail(final String className) {
        this(className, new String[0], Scope.Singleton);
    }

    public ClassDetail(final String className, final String[] dependencies) {
        this(className, dependencies, Scope.Singleton);
    }

    public ClassDetail(final String className, final Scope scope) {
        this(className, new String[0], scope);
    }

    public ClassDetail(final String className, final String[] dependencies, final Scope scope) {
        this.className = className;
        this.dependencies = dependencies;
        this.scope = scope;
    }

    public String getClassName() {
        return className;
    }


    public String[] getDependencies() {
        return dependencies;
    }


    public Scope getScope() {
        return scope;
    }

}
