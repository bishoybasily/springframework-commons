![Release](https://github.com/bishoybasily/springframework-commons/workflows/Release/badge.svg)

# Spring Framework Commons
General-purpose abstractions for spring-based projects

### Installation

##### enable the repository

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

##### add the dependencies you want

```xml
<dependencies>

    <dependency>
        <groupId>com.github.bishoybasily</groupId>
        <artifactId>springframework-commons-core</artifactId>
        <version>${springframework-commons.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.bishoybasily</groupId>
        <artifactId>springframework-commons-jpa</artifactId>
        <version>${springframework-commons.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.bishoybasily</groupId>
        <artifactId>springframework-commons-mongo</artifactId>
        <version>${springframework-commons.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.bishoybasily</groupId>
        <artifactId>springframework-commons-amqp</artifactId>
        <version>${springframework-commons.version}</version>
    </dependency>

    ...

</dependencies>
```



