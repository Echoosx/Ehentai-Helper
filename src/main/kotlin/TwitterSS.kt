package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.warning
import org.echoosx.mirai.plugin.command.TwitterScreenshotCommand
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin

object TwitterSS : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.Twitter-Helper",
        name = "Twitter-helper",
        version = "0.1.0"
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
            SeleniumConfig.reload()
            TwitterScreenshotCommand.register()
        }
    }
}
