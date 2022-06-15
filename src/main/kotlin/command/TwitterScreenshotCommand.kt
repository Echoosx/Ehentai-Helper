package org.echoosx.mirai.plugin.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.echoosx.mirai.plugin.TwitterSS
import org.echoosx.mirai.plugin.utils.screenshotTwitter

object TwitterScreenshotCommand:SimpleCommand(
    TwitterSS,
    "twitter", description = "推特截图"
) {
    private val logger get() = TwitterSS.logger
    @Suppress("unused")
    @Handler
    suspend fun CommandSender.handle(url: String) {
        try {
            sendMessage("正在获取推特内容...")

            val resource = screenshotTwitter(url).toExternalResource()
            subject?.sendImage(resource)
            withContext(Dispatchers.IO) {
                resource.close()
            }
        }catch (e:Throwable){
            sendMessage("获取失败")
            logger.error(e)
        }
    }
}