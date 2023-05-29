# dtox

DTOs combination generation library written in Groovy.

---

[![Java CI with Gradle](https://github.com/malikzh/dtox/actions/workflows/gradle.yml/badge.svg)](https://github.com/malikzh/dtox/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/malikzh/dtox/branch/main/graph/badge.svg?token=U51V1ZFD0M)](https://codecov.io/gh/malikzh/dtox)

---

## Features

- Can generate combinations of specified field values
- Deep DTO generation
- Well-tested library

## Install

Maven:
```xml
<dependency>
  <groupId>io.github.malikzh</groupId>
  <artifactId>dtox</artifactId>
  <scope>test</scope>
</dependency>
```

Gradle:

```groovy
testImplementation 'io.github.malikzh:dtox'
```

## Example

```groovy
import static io.github.malikzh.dtox.Dtox.dtox


// generate
List<ComplexDto> result = dtox(ComplexDto) {
    field1 'a', 'b'
    field2 {
        field2 4, 5
    }
}

// Generated data:
result[0].field1 == 'a'
result[0].field2.field2 == 4

result[1].field1 == 'a'
result[1].field2.field2 == 5

result[2].field1 == 'b'
result[2].field2.field2 == 4

result[3].field1 == 'b'
result[3].field2.field2 == 5

```

## Usage

```groovy

def value = Dtox.dtox(_Classname_) {
    somefield1 'value1', 'value2'
    somefield2 {
        somesubfield 1, 2, 3
    }
}

// ^ This example will be generated 6 DTO combinations

```

### Generate `nullable` values

```groovy
def value = Dtox.dtox(_Classname_, nullable: true) {
    somefield1 'value1', 'value2', nullable: true
}

// Will be generated 4 variants DTO, which first value will be null

```

### Using `excludeIf` closure

If closure `excludeIf` returns `false`, then dtox will not add dto to result collection. 
Closure accepts `dto` which you can use for checks.

```groovy
def result = dtox(SimpleDto, excludeIf: { SimpleDto it -> it.field2 == 6 }) {
    field1 'str1', 'str2'
    field2 4, 5, 6
}

// Will be generated 4 variants (without 6 in field2)
```

### Using `builder` closure

This closure accepts two arguments `Class<>` and `Map<String, Object>`.

- First argument is a field class
- Second argument is a map, which should be assigned to DTO.

Example:

DTO:
```java
@Data
public class SimpleDto {
    private String field1;
    private Integer field2;
    private SampleEnum field3;
}
```

Code:

```groovy

List<SimpleDto> result = dtox(SimpleDto, builder: {a, b -> createSimpleDto(a, b)}) {
    field1 'str1', 'str2'
    field2 4, 5
}

private SimpleDto createSimpleDto(Class clazz, Map<String, Object> data) {
    return new SimpleDto().tap {
        field1 = data['field1']
        field2 = (data['field2'] as Integer)
    }
}


// Will be generated DTOs using a builder closure.
```

## Usage with Spock

You can use this library with [Spock framework](https://spockframework.org/), like this:

```groovy

def 'some test'() {
    when:
    def result = someMethod(dto)
    
    then:
    // some checks
    
    where:
    [dto] << dtox(SomeDto) {
        field1 'a', 'b', 'c'
        field2 1, 2, 3
    }
}

```

## License

License: [MIT](LICENSE)

## Authors

- **Malik Zharykov** (Initial Work)

---

Made with ❤
