package io.github.yamin8000.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.User
import io.github.yamin8000.console.ConsoleHelper.getBooleanInput
import io.github.yamin8000.console.ConsoleHelper.getSingleString
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.utils.Constants
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Menus
import io.github.yamin8000.utils.Utility.pair
import io.github.yamin8000.utils.Utility.solo
import java.util.*

class AccountModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, Menus.accountMenu) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showAccountInfo()
            3 -> changeAccountBio()
        }

        run()
        return 0
    }

    private fun changeAccountBio() {
        val newBio = scanner.getSingleString("new bio: ")
        loading {
            igClient.actions.account().setBio(newBio).pair().solo({
                it()
                ter.println(resultStyle("bio changed to $newBio"))
            }, { it() })
        }
    }

    private fun showAccountInfo() {
        loading {
            igClient.actions.account().currentUser().pair().solo({
                printAccountInfo(it.user)
            }, { it() })
        }
    }

    private fun printAccountInfo(user: User) {
        UserModule.showShortUserInfo(user)
        checkIfMoreUserInfoNeeded(user)
    }

    private fun checkIfMoreUserInfoNeeded(userInfo: User) {
        ter.println(Constants.askStyle("Do you want to see full user info? (y/n)"))
        if (scanner.getBooleanInput()) UserModule.showFullUserInfo(userInfo)
    }
}