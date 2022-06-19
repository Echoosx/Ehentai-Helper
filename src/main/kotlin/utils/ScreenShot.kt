package org.echoosx.mirai.plugin.utils

import com.assertthat.selenium_shutterbug.core.CaptureElement
import com.assertthat.selenium_shutterbug.core.Shutterbug
import org.echoosx.mirai.plugin.FeedConfig.authToken
import org.echoosx.mirai.plugin.SeleniumConfig
import org.echoosx.mirai.plugin.TwitterSS
import org.echoosx.mirai.plugin.TwitterSS.dataFolderPath
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.openqa.selenium.NoSuchElementException
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin
import xyz.cssxsh.selenium.hide
import java.io.File
import java.io.InputStream
import java.lang.Thread.sleep

internal val logger get() = TwitterSS.logger

fun screenshotTwitter(twitterLink:String):InputStream{
    val driver = MiraiSeleniumPlugin.driver(config = SeleniumConfig)
    val filename: String
    val twitterRegex = Regex("""https?://(mobile.)?twitter.com/.+?/status/(\d+)""")
    if(twitterLink.matches(twitterRegex)){
        filename = twitterRegex.matchEntire(twitterLink)!!.groupValues[2]
    }else
        throw Exception("不是twitter链接")
    try {
        driver.get(twitterLink)
        val cookies = listOf(
            Pair("night_mode","0"),
            Pair("auth_token", authToken),
            Pair("lang","ja"),
        )
        for(cookie in cookies){
            driver.manage().addCookie(Cookie(cookie.first,cookie.second))
        }
        driver.get(twitterLink)
        sleep(5_000)
        driver.hide(
            "div.css-1dbjc4n.r-aqfbo4.r-1p0dtai.r-1d2f490.r-12vffkv.r-1xcajam.r-zchlnj",
            "div.css-1dbjc4n.r-aqfbo4.r-gtdqiz.r-1gn8etr.r-1g40b8q",
        )
//        val article =
//            driver.findElement(By.cssSelector("div.css-1dbjc4n.r-14lw9ot.r-jxzhtn.r-1ljd8xs.r-13l2t4g.r-1phboty.r-1jgb5lz.r-11wrixw.r-61z16t.r-1ye8kvj.r-13qz1uu.r-184en5c > div > section > div > div > div:nth-child(1)"))
        val article = driver.findElement(By.cssSelector("article"))
        Shutterbug.shootElement(driver, article, CaptureElement.FULL_SCROLL).withName(filename)
            .save("${dataFolderPath}/twitter")

    } catch (e: NoSuchElementException){
        sleep(5_000)
//        val article =
//            driver.findElement(By.cssSelector("div.css-1dbjc4n.r-14lw9ot.r-jxzhtn.r-1ljd8xs.r-13l2t4g.r-1phboty.r-1jgb5lz.r-11wrixw.r-61z16t.r-1ye8kvj.r-13qz1uu.r-184en5c > div > section > div > div > div:nth-child(1)"))
        val article = driver.findElement(By.cssSelector("article"))
        Shutterbug.shootElement(driver, article, CaptureElement.FULL_SCROLL).withName(filename)
            .save("${dataFolderPath}/twitter")
    }finally {
        driver.quit()
    }
    return File("${dataFolderPath}/twitter/${filename}.png").inputStream()
}


fun touchDir(dirPath: String): Boolean {
    var destDirName = dirPath
    val dir = File(destDirName)
    if (dir.exists()) {
        return false
    }
    if (!destDirName.endsWith(File.separator)) {
        destDirName += File.separator
    }
    //创建目录
    return if (dir.mkdirs()) {
        true
    } else {
        logger.error("创建目录" + destDirName + "失败！")
        false
    }
}
