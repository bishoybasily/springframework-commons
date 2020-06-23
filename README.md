![Maven Package](https://github.com/bishoybasily/springframework-commons/workflows/Maven%20Package/badge.svg)

# Spring Framework Commons
Some abstractions for the common logic while working with spring framework

### Installation

##### enable the sonatype repositories

```xml
<repository>
    <id>sonatype</id>
    <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
</repository>
<repository>
    <id>sonatype-snapshot</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

##### add the dependencies you want

```xml
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
```
