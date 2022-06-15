package org.echoosx.mirai.plugin.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import org.dom4j.io.SAXReader
import org.echoosx.mirai.plugin.FeedConfig.host
import org.echoosx.mirai.plugin.FeedConfig.port
import org.echoosx.mirai.plugin.FeedConfig.rsshub
import org.echoosx.mirai.plugin.FeedConfig.timeout
import org.echoosx.mirai.plugin.data.RecordData
import org.xml.sax.InputSource
import java.io.StringReader
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

class Twitter{
    var author:String? = null
    var title:String? = null
    var description:String? = null
    var pubDate:String? = null
    var link:String? = null
}

fun getLatestTwitter(channelId:String):Twitter{
    val xml:String = connectHttpGet("https://${rsshub}/twitter/media/${channelId}")

    val xmlMap = HashMap<String,String>()
    xmlMap["atom"] = "http://www.w3.org/2005/Atom"

    val reader = SAXReader()
    reader.documentFactory.xPathNamespaceURIs = xmlMap
    val document = reader.read(InputSource(StringReader(xml)))
    val twitter = Twitter()
    twitter.author = document.selectSingleNode("//item/author").text
    twitter.title = document.selectSingleNode("//item/title").text
    twitter.description = document.selectSingleNode("//item/description").text
    twitter.pubDate = document.selectSingleNode("//item/pubDate").text
    twitter.link = document.selectSingleNode("//item/link").text

    return twitter
}

fun checkUpdate(channelId: String):Twitter?{
    val twitter = getLatestTwitter(channelId)
    return if(twitter.link != RecordData.record[channelId]?.latest && twitter.link != RecordData.record[channelId]?.pre)
        twitter
    else
        null
}

// Http Get方法
internal fun connectHttpGet(url: String) :String {
    var tempString = ""
    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
    val client = OkHttpClient().newBuilder()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .proxy(proxy)
        .build()

    val request = Request.Builder()
        .url(url)
        .get()
        .build()
    //同步处理
    val call = client.newCall(request)
    val response = call.execute()
    tempString = response.body?.string().toString()

    return tempString
}