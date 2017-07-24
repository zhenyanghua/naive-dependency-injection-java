# Naive Implementation of Dependency Injection in Java

## Usage
```java
public class Main {

    private static final String PKG_DEPOSIT_DAO = "io.hua.test.dao.DepositDaoImpl";
    private static final String PKG_DEPOSIT_SERVICE = "io.hua.test.service.DepositServiceImpl";
    private static final String PKG_DEPOSIT = "io.hua.test.domain.Deposit";

    public static void main(String[] args) {

        // Create an array of ClassDetail object
        ClassDetail[] classDetails = new ClassDetail[] {
            new ClassDetail(PKG_DEPOSIT_DAO),
            new ClassDetail(PKG_DEPOSIT_SERVICE, new String[] {PKG_DEPOSIT_DAO}),
            new ClassDetail(PKG_DEPOSIT, Scope.Prototype)
        };

        try {
            // Get the context singleton and initialize it by registering all classes.
            ApplicationContext context = ApplicationContext.CONTEXT;
            context.init(classDetails);

            // Call getInstance method
            DepositService depositService_1 = context.getInstance(DepositService.class);
            Deposit deposit_1 = context.getInstance(Deposit.class);
            deposit_1.setAmount(100);
            depositService_1.createDeposit(deposit_1);

            // Call getInstance method
            DepositService depositService_2 = context.getInstance(DepositService.class);
            Deposit deposit_2 = context.getInstance(Deposit.class);
            deposit_2.setAmount(60);
            depositService_2.createDeposit(deposit_2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## API
### ApplicationContext Singleton Class
|Values|Description|
|------|-----------|
|**CONTEXT**|The singleton instance of the class|

|Methods|Description|
|-------|-----------|
|**init(final ClassDetail[] details)**|Return Value: `void` - Initializes the context by registering all passed classes.|
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
|**getClassName()**|Type: `String` - The instances of which to be managed. String value ust be full class path including package name.|
|**getDependencies()**|Type: `[String, <...>]` - An array of dependent classes. String value must be full class path including package name.|
|**getScope()**|Type: `Scope` - the scope of the class instance. Defaults to `Scope.Singleton`.|

### Scope Enum
|Values|Description|
|------|-----------|
|**Singleton**|This creates only one shared instance in the container context.|
|**Prototype**|This creates a new instance in the container context every time when an instance is fetched.|