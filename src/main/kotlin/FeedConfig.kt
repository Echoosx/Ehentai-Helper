package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object FeedConfig:ReadOnlyPluginConfig("feed") {
    @ValueDescription("代理ip")
    val host:String by value("127.0.0.1")

    @ValueDescription("代理端口")
    val port:Int by value(7890)

    @ValueDescription("请求超时时间（s）")
    val timeout:Long by value(30L)

    @ValueDescription("RssHub服务器")
    val rsshub:String by value("rsshub.app")

    @ValueDescription("每次启动时是否刷新record（选择是则bot关闭时的更新丢弃）")
    val refresh:Boolean by value(true)
}