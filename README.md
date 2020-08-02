![Release](https://github.com/bishoybasily/springframework-commons/workflows/Release/badge.svg)

# Spring Framework Commons
General-purpose abstractions for spring-based projects

### Installation

##### enable the repository

```xml
<repositories>
    <repository>
        <id>github-bishoybasily-springframework-commons</id>
        <url>https://raw.github.com/bishoybasily/springframework-commons/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

##### add the dependencies you want

```xml
...
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
```
