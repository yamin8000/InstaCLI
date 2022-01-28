package yamin.utils

import yamin.console.printlnC
import yamin.console.ConsoleHelper.getIntegerInput
import yamin.utils.Constants.animations
import yamin.utils.Constants.currentLoadingAnimation
import yamin.utils.Constants.settingSubmenuText
import java.util.*

class Settings(private val scanner: Scanner) {

    init {
        showSettings()
        handleSubMenuInput()
    }

    private fun handleSubMenuInput() {
        when (scanner.getIntegerInput()) {
            0 -> handleLoadingAnimationSelection()
        }
    }

    private fun handleLoadingAnimationSelection() {
        animations.forEachIndexed { index, item ->
            printlnC { "$index. $item" }
        }
        val input = scanner.getIntegerInput()
        currentLoadingAnimation = input
        printlnC { "Current loading animation changed to : ${animations[input]}".green }
    }


    private fun showSettings() {
        printlnC { settingSubmenuText.bold.red }
    }
}