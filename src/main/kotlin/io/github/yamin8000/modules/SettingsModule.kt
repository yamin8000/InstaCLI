package io.github.yamin8000.modules

import io.github.yamin8000.console.ConsoleHelper.getIntegerInput
import io.github.yamin8000.utils.Constants.animations
import io.github.yamin8000.utils.Constants.currentLoadingAnimation
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Menus.settingSubmenuText
import java.util.*

class SettingsModule(scanner: Scanner) : BaseModule(scanner, settingSubmenuText) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> changeLoadingAnimationType()
        }

        run()
        return 0;
    }

    private fun changeLoadingAnimationType() {
        animations.forEachIndexed { index, item ->
            ter.println("$index. $item")
        }
        val input = scanner.getIntegerInput()
        currentLoadingAnimation = input
        ter.println("Current loading animation changed to : ${animations[input]}")
    }
}