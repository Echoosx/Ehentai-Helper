package org.echoosx.mirai.plugin.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

@Serializable
data class TwitterSubscribeRecord(
    @SerialName("contact")
    var contacts: MutableList<Long> = arrayListOf(),

    @SerialName("latest")
    var latest:String = "",

    @SerialName("pre")
    var pre:String = ""
)

object RecordData:AutoSavePluginData("Subscribe") {
    @ValueDescription("订阅记录")
    val record:MutableMap<String,TwitterSubscribeRecord> by value()
}