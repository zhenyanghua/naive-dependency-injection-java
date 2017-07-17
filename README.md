# Naive Implementation of Dependency Injection in Java

## Usage
```java
public class Main {

    private static final String PKG_DEPOSIT_DAO = "io.hua.test.DepositDao";
    private static final String PKG_DEPOSIT_SERVICE = "io.hua.test.DepositService";

    public static void main(String[] args) {

        // Create an array of ClassDetail object
        ClassDetail[] classDetails = new ClassDetail[] {
            new ClassDetail(PKG_DEPOSIT_DAO),
            new ClassDetail(PKG_DEPOSIT_SERVICE, new String[] {PKG_DEPOSIT_DAO})
        };

        try {
            // Get the context singleton through the factory method.
            Container context = Container.getContext(classDetails);

            // Call getInstance method
            DepositService depositService_1 = context.getInstance(DepositService.class);
            depositService_1.createDeposit(100);

            DepositService depositService_2 = context.getInstance(DepositService.class);
            depositService_2.createDeposit(60);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## API
### Container Class
|Methods|Description|
|-------|-----------|
|**getContext()**|Return Value: `Container` - Fetches the singleton context that manages all registered class instances.|
|**getClass()**|Return Value: `Class<T>` - Fetches the scoped instance of the class.|

### ClassDetail Class
|Constructors|Description|
|-----------|-----------|
|**ClassDetail(final String className)**|constructor method|
|**ClassDetail(final String className, final String[] dependencies)**|constructor method|
|**ClassDetail(final String className, final Scope scope)**|constructor method|
|**ClassDetail(final String className, final String[] dependencies, final Scope scope)**|constructor method|

|Methods|Description|
|-------|-----------|
|**getClassName()**|Type: `String` - The instances of which to be managed.|
|**getDependencies()**|Type: `[String, <...>]` - An array of dependent classes.|
|**getScope()**|Type: `Scope` - the scope of the class instance. Defaults to `Scope.Singleton`.|

### Scope Enum
|Values|Description|
|------|-----------|
|Singleton|This creates only one shared instance in the container context.|
|Prototype|This creates a new instance in the container context every time when an instance is fetched.|