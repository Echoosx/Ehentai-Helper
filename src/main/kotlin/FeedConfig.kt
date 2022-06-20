package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object FeedConfig:ReadOnlyPluginConfig("feed") {
    @ValueDescription("开机自动刷新记录")
    val refresh:Boolean by value(false)

    @ValueDescription("twitter cookie auth_token")
    val authToken:String by value()

    @ValueDescription("高频轮询")
    val poll:String by value("0 0/2 15-18 * * ?")

    @ValueDescription("低频轮询")
    val lazyPoll:String by value("0 0/15 0-14,18-23 * * ?")
}