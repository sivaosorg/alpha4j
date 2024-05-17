# alpha4j

## Introduction

alpha4j: is a Java 8 library featuring common data structures and algorithms. Enhance your projects with efficient and
easy-to-use implementations designed for performance and clarity.

## Features

- Comprehensive set of utility functions.
- Written in Java 1.8.
- Well-documented code for easy understanding.
- Regular updates and maintenance.

## Installation

```bash
git clone --depth 1 https://github.com/sivaosorg/alpha4j.git
```

## Generation Plugin Java

```bash
curl https://gradle-initializr.cleverapps.io/starter.zip -d type=groovy-gradle-plugin  -d testFramework=testng -d projectName=alpha4j -o alpha4j.zip
```

## Modules

Explain how users can interact with the various modules.

### Tidying up

To tidy up the project's Java modules, use the following command:

```bash
./gradlew clean
```

or

```bash
make clean
```

### Building SDK

```bash
./gradlew jar
```

or

```bash
make jar
```

### Upgrading version

- file `gradle.properties`

```sh
ng.name=alpha4j
ng.version=v1.0.0
```

## Integration

1. Add dependency into file `build.gradle`

```gradle
implementation files('libs/alpha4j-v1.0.0.jar') // filename based on ng.name and ng.version
```

2. Edit file `main Spring Boot application` (optional)

```java

@SpringBootApplication
@ComponentScan(basePackages = {"org.alpha4j"}) // root name of package alpha4j
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
```
