package org.echoosx.mirai.plugin.utils

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.ContactUtils.getFriendOrGroup
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.echoosx.mirai.plugin.FeedConfig.authToken
import org.echoosx.mirai.plugin.SeleniumConfig
import org.echoosx.mirai.plugin.TwitterSS
import org.echoosx.mirai.plugin.data.RecordData
import org.openqa.selenium.Cookie
import org.openqa.selenium.remote.RemoteWebDriver
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin
import java.lang.Exception

internal class Subscribe: Job {
    private val driver = MiraiSeleniumPlugin.driver(config = SeleniumConfig)
    init {
        driver.get("https://twitter.com")
        val cookies = listOf(
            Pair("night_mode","0"),
            Pair("auth_token", authToken),
            Pair("lang","ja"),
        )
        for(cookie in cookies){
            driver.manage().addCookie(Cookie(cookie.first,cookie.second))
        }
    }
    @Throws(JobExecutionException::class)
    override fun execute(jobExecutionContext: JobExecutionContext?) {
        try {
            Bot.instances.filter { it.isOnline }.forEach { bot ->
                bot.subscribe(this.driver)
            }
        }catch (e: Exception){
            logger.error("$e")
        }
    }
}

@OptIn(ConsoleExperimentalApi::class)
internal fun Bot.subscribe(driver: RemoteWebDriver) = TwitterSS.launch {
    RecordData.record.forEach { record->
        val link = checkUpdate(driver,record.key)
        if(link != null){
            val resource = link.let { screenshotTwitter(driver,it).toExternalResource() }
            record.value.contacts.forEach{ id->
                bot.getFriendOrGroup(id).sendImage(resource)
            }
            resource.close()
            record.value.pre = record.value.latest
            record.value.latest = link
        }
    }
}