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