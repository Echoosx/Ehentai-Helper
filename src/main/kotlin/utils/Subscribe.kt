package org.echoosx.mirai.plugin.utils

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.ContactUtils.getFriendOrGroup
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.echoosx.mirai.plugin.TwitterSS
import org.echoosx.mirai.plugin.data.RecordData
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import java.lang.Exception

internal class Subscribe: Job {
    @Throws(JobExecutionException::class)
    override fun execute(jobExecutionContext: JobExecutionContext?) {
        try {
            Bot.instances.filter { it.isOnline }.forEach { bot ->
                bot.subscribe()
            }
        }catch (e: Exception){
            logger.error("$e")
        }
    }
}

@OptIn(ConsoleExperimentalApi::class)
internal fun Bot.subscribe() = TwitterSS.launch {
    RecordData.record.forEach { record->
        val link = checkUpdate(record.key)
        if(link != null){
            val resource = link.let { screenshotTwitter(it).toExternalResource() }
            record.value.contacts.forEach{ id->
                bot.getFriendOrGroup(id).sendImage(resource)
            }
            resource.close()
            record.value.pre = record.value.latest
            record.value.latest = link
        }
    }
}