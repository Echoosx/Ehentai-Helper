package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object FeedConfig:ReadOnlyPluginConfig("feed") {
    @ValueDescription("开机自动刷新记录")
    val refresh:Boolean by value(false)

    @ValueDescription("每次轮询间隔（min）")
    val during:Int by value(2)

    @ValueDescription("twitter cookie auth_token")
    val authToken:String by value()
}