package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.warning
import org.echoosx.mirai.plugin.FeedConfig.lazyPoll
import org.echoosx.mirai.plugin.FeedConfig.poll
import org.echoosx.mirai.plugin.FeedConfig.refresh
import org.echoosx.mirai.plugin.command.TwitterScreenshotCommand
import org.echoosx.mirai.plugin.data.RecordData
import org.echoosx.mirai.plugin.utils.Subscribe
import org.echoosx.mirai.plugin.utils.getLatestTwitterLink
import org.echoosx.mirai.plugin.utils.touchDir
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin

object TwitterSS : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.Twitter-Helper",
        name = "Twitter-helper",
        version = "1.0.0"
    ) {
        author("Echoosx")
        dependsOn("xyz.cssxsh.mirai.plugin.mirai-selenium-plugin", true)
    }
) {
    private val selenium: Boolean by lazy {
        try {
            MiraiSeleniumPlugin.setup()
        } catch (exception: NoClassDefFoundError) {
            logger.warning { "相关类加载失败，请安装 https://github.com/cssxsh/mirai-selenium-plugin $exception" }
            false
        }
    }

    override fun onEnable() {
        if(selenium) {
            FeedConfig.reload()
            RecordData.reload()
            SeleniumConfig.reload()
            TwitterScreenshotCommand.register()

            touchDir("${dataFolderPath}/twitter")

            if(refresh){
                RecordData.record.forEach{ record->
                    val link = getLatestTwitterLink(record.key)
                    record.value.latest = link
                }
            }

            val scheduler = StdSchedulerFactory.getDefaultScheduler()
            val jobDetail = JobBuilder.newJob(Subscribe::class.java)
                .withIdentity("subscribe","group1")
                .storeDurably(true)
                .build()
            scheduler.addJob(jobDetail,false)

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity("poll","group1")
                .withSchedule(
                    CronScheduleBuilder.cronSchedule(poll)
                )
                .forJob(jobDetail)
                .build()

            val lazyTrigger = TriggerBuilder.newTrigger()
                .withIdentity("lazy","group1")
                .withSchedule(
                    CronScheduleBuilder.cronSchedule(lazyPoll)
                )
                .forJob(jobDetail)
                .build()

            scheduler.scheduleJob(trigger)
            scheduler.scheduleJob(lazyTrigger)
            scheduler.start()
        }
    }
}
