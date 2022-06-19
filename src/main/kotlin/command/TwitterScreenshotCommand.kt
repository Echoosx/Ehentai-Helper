package org.echoosx.mirai.plugin.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.echoosx.mirai.plugin.SeleniumConfig
import org.echoosx.mirai.plugin.TwitterSS
import org.echoosx.mirai.plugin.utils.screenshotTwitter
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin

object TwitterScreenshotCommand:SimpleCommand(
    TwitterSS,
    "twitter", description = "推特截图"
) {
    private val logger get() = TwitterSS.logger
    @Suppress("unused")
    @Handler
    suspend fun CommandSender.handle(url: String,contact: Contact = subject as Contact) {
        try {
            subject?.sendMessage("正在获取推特内容...")
            val driver = MiraiSeleniumPlugin.driver(config = SeleniumConfig)
            val resource = screenshotTwitter(url).toExternalResource()
            contact.sendImage(resource)
            withContext(Dispatchers.IO) { resource.close() }
            driver.quit()
        }catch (e:Throwable){
            sendMessage("获取失败")
            logger.error(e)
        }
    }
}