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
<project xmlns="http://maven.apache.org/POM/4.0.0">

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

