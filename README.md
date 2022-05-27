![Release](https://github.com/bishoybasily/springframework-commons/workflows/Release/badge.svg)

# Spring Framework Commons

General-purpose abstractions for spring-based projects

### Installation

#### enable sonatype repositories

```xml

<repositories>

    <repository>
        <id>sonatype</id>
        <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
    <repository>
        <id>sonatype-snapshot</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>

    ...

</repositories>
```

#### add the dependencies

```xml

<project>

    <dependencies>

        <dependency>
            <groupId>com.github.bishoybasily</groupId>
            <artifactId>springframework-commons-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.bishoybasily</groupId>
            <artifactId>springframework-commons-jpa</artifactId>
        </dependency>

        ...

    </dependencies>

    <dependencyManagement>
        <dependencies>

            <dependency>
                <groupId>com.github.bishoybasily</groupId>
                <artifactId>springframework-commons</artifactId>
                <version>${springframework-commons.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            ...

        </dependencies>
    </dependencyManagement>

</project>
```

### JpaService example for an entity called Device

#### example device params

```java

@Getter
@Setter
@Accessors(chain = true)
public class DeviceParams extends Params {

    private String userId;
    private Boolean approved;

    // more params to be used for building the specification

}
```

#### example jpa repository

```java
public interface RepositoryDevices extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device> {
}
```

#### example jpa service

```java

@RequiredArgsConstructor
@Service
public class ServiceDevices implements JpaService<Device, String, DeviceParams> {

    // this lombok getter will satisfy the `getJpaRepository` implementation 
    @Getter
    private final RepositoryDevices jpaRepository;

    // let's do some cleanup before the executing create (check other pre-post methods)
    @Override
    public Mono<Device> preCreate(Device device) {
        return Mono.just(device);
    }

    // or let's fire an event after the update (check other pre-post methods)
    @Override
    public Mono<Device> postUpdate(Device device) {
        return Mono.just(device);
    }

    // create the specification from the params object, 
    // you don't need to worry about the sort & pagination params, they will be handled if presented (check the implementation of CollectionRequest).
    // the goal here is to build a Specification object from the params and return it as Optional.
    @Override
    public Optional<Specification<Device>> getSpecification(DeviceParams params) {
        return new ArrayList<Specification<Device>>() {{
            String userId = params.getUserId();
            if (!ObjectUtils.isEmpty(userId))
                add((root, query, builder) -> {
                    return builder.equal(root.join("user").get("id"), userId);
                });
            Boolean approved = params.getApproved();
            if (!ObjectUtils.isEmpty(approved))
                add((root, query, builder) -> {
                    return builder.equal(root.get("approved"), approved);
                });
        }}.stream().reduce(Specification::and);
    }

    @Override
    public JpaSpecificationExecutor<Device> getJpaSpecificationExecutor() {
        return jpaRepository;
    }

    @Override
    public Supplier<RuntimeException> supplyNotFoundException(String id) {
        return () ->
                new RuntimeException(String.format("Couldn't find a device with reference (%s)", id));
    }

}
```
