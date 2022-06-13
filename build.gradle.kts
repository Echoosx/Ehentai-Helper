plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.0"
}

group = "org.echoosx"
version = "0.1.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}

dependencies{
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.assertthat:selenium-shutterbug:1.6")
    compileOnly("xyz.cssxsh.mirai:mirai-selenium-plugin:2.1.0")
    testImplementation("xyz.cssxsh.mirai:mirai-selenium-plugin:2.1.0")
}