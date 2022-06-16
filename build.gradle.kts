plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.0"
}

group = "org.echoosx"
version = "1.0.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}

dependencies{
    implementation("dom4j:dom4j:1.6.1")
    implementation("jaxen:jaxen:1.2.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.assertthat:selenium-shutterbug:1.6")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    compileOnly("xyz.cssxsh.mirai:mirai-selenium-plugin:2.1.1")
    testImplementation("xyz.cssxsh.mirai:mirai-selenium-plugin:2.1.1")
    testImplementation(kotlin("test"))
}