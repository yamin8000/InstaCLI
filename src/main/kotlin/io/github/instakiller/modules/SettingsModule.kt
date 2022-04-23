package io.github.instakiller.modules

import io.github.instakiller.console.ConsoleHelper.readBoolean
import io.github.instakiller.console.ConsoleHelper.readInteger
import io.github.instakiller.console.ConsoleHelper.readSingleString
import io.github.instakiller.helpers.LoggerHelper.loading
import io.github.instakiller.utils.Constants.DOWNLOAD_FOLDER
import io.github.instakiller.utils.Constants.LOADING_ANIMATION
import io.github.instakiller.utils.Constants.SESSION_AUTOSAVE
import io.github.instakiller.utils.Constants.animations
import io.github.instakiller.utils.Constants.currentLoadingAnimation
import io.github.instakiller.utils.Constants.downloadDir
import io.github.instakiller.utils.Constants.errorStyle
import io.github.instakiller.utils.Constants.isAutosavingSession
import io.github.instakiller.utils.Constants.resultStyle
import io.github.instakiller.utils.Constants.ter
import io.github.instakiller.utils.Constants.warningStyle
import io.github.instakiller.utils.FileUtils.createDirIfNotExists
import io.github.instakiller.utils.Menus.settingSubmenuText
import java.io.File

class SettingsModule : BaseModule(settingSubmenuText) {

    private val configRegex = Regex("(.+[=].+\\n*)+")

    private val configPath = "config/config.env"

    private val configFile = File(configPath)

    private val configPairs by lazy { loadConfigPairs() }

    init {
        createDirIfNotExists("config")
        createConfigFileIfNecessary()
        loadConfigToMemory()
    }

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> changeLoadingAnimationType()
            3 -> changeDownloadFolder()
            4 -> changeSessionAutosave()
        }

        run()
        return 0
    }

    private fun changeLoadingAnimationType() {
        ter.println(warningStyle("Current loading animation is: ${animations[currentLoadingAnimation]}"))
        animations.forEachIndexed { index, item -> ter.println("$index. $item") }
        val input = readInteger()
        currentLoadingAnimation = input
        ter.println(resultStyle("Current loading animation changed to : ${animations[input]}"))
        updateConfigFile(LOADING_ANIMATION to input)
    }

    private fun changeDownloadFolder() {
        ter.println(warningStyle("Current download folder is : $downloadDir"))
        downloadDir = readSingleString("new download folder: ")
        ter.println(resultStyle("Current download folder changed to : $downloadDir"))
        updateConfigFile(DOWNLOAD_FOLDER to downloadDir)
    }

    private fun changeSessionAutosave() {
        ter.println(warningStyle("Current session autosave is : $isAutosavingSession"))
        isAutosavingSession = readBoolean("Enable session autosave? (y/n)")
        if (isAutosavingSession) ter.println(resultStyle("Session autosave enabled"))
        else ter.println(resultStyle("Session autosave disabled"))
        updateConfigFile(SESSION_AUTOSAVE to isAutosavingSession)
    }

    private fun createConfigFileIfNecessary() {
        when {
            !isConfigFileExists() -> initConfigFile()
            !isConfigFileValid() -> handleInvalidConfigFile()
        }
    }

    private fun initConfigFile() {
        configFile.createNewFile()
        configFile.writeText(
            """
                $LOADING_ANIMATION=$currentLoadingAnimation
                $DOWNLOAD_FOLDER=$downloadDir
                $SESSION_AUTOSAVE=$isAutosavingSession
                """.trimIndent()
        )
    }

    private fun handleInvalidConfigFile() {
        ter.println(errorStyle("Config file is invalid, trying to recover it..."))
        loading {
            val invalidConfigFile = File("$configPath.invalid")
            invalidConfigFile.writeText(configFile.readText())
            val recoveredConfigFile = configRegex.find(configFile.readText())?.value
            if (recoveredConfigFile != null) configFile.writeText(recoveredConfigFile)
            else createConfigFileIfNecessary()
            it()
        }
    }

    private fun isConfigFileExists(): Boolean {
        val configFile = File(configPath)
        return configFile.isFile
    }

    private fun isConfigFileValid(): Boolean {
        val configFile = File(configPath)
        val configFileContent = configFile.readText().trim()
        return configRegex.matches(configFileContent)
    }

    private fun updateConfigFile(configPair: Pair<String, Any>) {
        configPairs.listIterator().let { iterator ->
            while (iterator.hasNext()) {
                if (iterator.next().first == configPair.first) {
                    iterator.set(configPair.first to configPair.second.toString())
                }
            }
        }
        configFile.writeText(configPairs.joinToString("\n") {
            "${it.first}=${it.second}"
        })
    }

    private fun loadConfigPairs(): MutableList<Pair<String, String>> {
        return configFile.readLines().map { line ->
            val pair = line.trim().split("=").take(2)
            if (pair.size != 2) throw IllegalArgumentException("Invalid config file")
            pair.first() to pair.last()
        }.toMutableList()
    }

    private fun loadConfigToMemory() {
        configPairs.forEach {
            when (it.first) {
                LOADING_ANIMATION -> currentLoadingAnimation = it.second.toInt()
                DOWNLOAD_FOLDER -> downloadDir = it.second
                SESSION_AUTOSAVE -> isAutosavingSession = it.second.toBoolean()
            }
        }
    }
}