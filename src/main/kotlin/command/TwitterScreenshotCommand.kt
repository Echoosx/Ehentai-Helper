package org.echoosx.mirai.plugin.command

import com.assertthat.selenium_shutterbug.core.CaptureElement
import com.assertthat.selenium_shutterbug.core.Shutterbug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import org.echoosx.mirai.plugin.SeleniumConfig
import org.echoosx.mirai.plugin.TwitterSS
import org.echoosx.mirai.plugin.TwitterSS.dataFolderPath
import org.openqa.selenium.By
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin
import xyz.cssxsh.selenium.hide
import java.io.File
import java.lang.Thread.sleep
import org.openqa.selenium.NoSuchElementException

object TwitterScreenshotCommand:SimpleCommand(
    TwitterSS,
    "twitter", description = "推特截图"
) {
    private val logger get() = TwitterSS.logger
    @Suppress("unused")
    @Handler
    suspend fun CommandSender.handle(url: String) {
//        sendMessage("正在获取推特...")
        val driver = MiraiSeleniumPlugin.driver(config = SeleniumConfig)
        try {
            driver.get(url)
//            withContext(Dispatchers.IO) { sleep(3_000) }
            driver.hide(
                "div.css-1dbjc4n.r-aqfbo4.r-1p0dtai.r-1d2f490.r-12vffkv.r-1xcajam.r-zchlnj",
                "div.css-1dbjc4n.r-aqfbo4.r-gtdqiz.r-1gn8etr.r-1g40b8q",
            )
            val article =
                driver.findElement(By.cssSelector("div.css-1dbjc4n.r-14lw9ot.r-jxzhtn.r-1ljd8xs.r-13l2t4g.r-1phboty.r-1jgb5lz.r-11wrixw.r-61z16t.r-1ye8kvj.r-13qz1uu.r-184en5c > div > section > div > div > div:nth-child(1)"))
            Shutterbug.shootElement(driver, article, CaptureElement.FULL_SCROLL).withName("twitter")
                .save("$dataFolderPath")
            subject?.sendImage(File("${dataFolderPath}/twitter.png"))

        } catch (e:NoSuchElementException){
            print("plus!")
            withContext(Dispatchers.IO) { sleep(5_000) }
            val article =
                driver.findElement(By.cssSelector("div.css-1dbjc4n.r-14lw9ot.r-jxzhtn.r-1ljd8xs.r-13l2t4g.r-1phboty.r-1jgb5lz.r-11wrixw.r-61z16t.r-1ye8kvj.r-13qz1uu.r-184en5c > div > section > div > div > div:nth-child(1)"))
            Shutterbug.shootElement(driver, article, CaptureElement.FULL_SCROLL).withName("twitter")
                .save("$dataFolderPath")
            subject?.sendImage(File("${dataFolderPath}/twitter.png"))
        } catch (e:Throwable){
            sendMessage("推特获取失败")
            logger.error(e)
        } finally {
            driver.quit()
        }
    }
}