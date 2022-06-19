package org.echoosx.mirai.plugin.utils

import org.echoosx.mirai.plugin.FeedConfig.authToken
import org.echoosx.mirai.plugin.SeleniumConfig
import org.echoosx.mirai.plugin.data.RecordData
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.openqa.selenium.NoSuchElementException
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin
import java.lang.Thread.sleep


fun getLatestTwitterLink(url:String):String{
    val driver = MiraiSeleniumPlugin.driver(config = SeleniumConfig)
    try {
        driver.get(url)
        val cookies = listOf(
            Pair("night_mode","0"),
            Pair("auth_token", authToken),
            Pair("lang","ja"),
        )
        for(cookie in cookies){
            driver.manage().addCookie(Cookie(cookie.first,cookie.second))
        }
        driver.get(url)
        sleep(3_000)

        val a = driver.findElement(By.cssSelector(
            "a.css-4rbku5.css-18t94o4.css-901oao.r-1loqt21.r-1q142lx.r-a023e6.r-16dba41.r-rjixqe.r-bcqeeo.r-3s2u2q.r-qvutc0"
        ))
        return a.getAttribute("href")
    }catch (e:NoSuchElementException){
        sleep(5_000)
        val a = driver.findElement(By.cssSelector(
            "a.css-4rbku5.css-18t94o4.css-901oao.r-1loqt21.r-1q142lx.r-a023e6.r-16dba41.r-rjixqe.r-bcqeeo.r-3s2u2q.r-qvutc0"
        ))
        return a.getAttribute("href")
    }finally {
        driver.quit()
    }
}


fun checkUpdate(channelId: String):String?{
    val link = getLatestTwitterLink("https://twitter.com/$channelId/media")
    return if(link != RecordData.record[channelId]?.latest && link != RecordData.record[channelId]?.pre)
        link
    else
        null
}

// Http Get方法
//internal fun connectHttpGet(url: String) :String {
//    var tempString = ""
//    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
//    val client = OkHttpClient().newBuilder()
//        .connectTimeout(timeout, TimeUnit.SECONDS)
//        .proxy(proxy)
//        .build()
//
//    val request = Request.Builder()
//        .url(url)
//        .get()
//        .cacheControl(CacheControl.Builder().noStore().build())
//        .build()
//    //同步处理
//    val call = client.newCall(request)
//    val response = call.execute()
//    tempString = response.body?.string().toString()
//
//    return tempString
//}

/* 弃用rsshub监测
class Twitter{
    var author:String? = null
    var title:String? = null
    var description:String? = null
    var pubDate:String? = null
    var link:String? = null
}

fun getLatestTwitter(channelId:String,debug:Boolean = false):Twitter{
    val xml:String = connectHttpGet("https://${rsshub}/twitter/media/${channelId}")
    if(debug){ logger.info(xml) }
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
    logger.info("$channelId latest:${twitter.link}")
    return if(twitter.link != RecordData.record[channelId]?.latest && twitter.link != RecordData.record[channelId]?.pre)
        twitter
    else
        null
}
*/