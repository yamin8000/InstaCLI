package io.github.instakiller.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.User
import io.github.instakiller.console.ConsoleHelper.readBoolean
import io.github.instakiller.console.ConsoleHelper.readSingleString
import io.github.instakiller.helpers.LoggerHelper.loading
import io.github.instakiller.utils.Constants
import io.github.instakiller.utils.Constants.resultStyle
import io.github.instakiller.utils.Constants.ter
import io.github.instakiller.utils.Menus
import io.github.instakiller.utils.Utility.pair
import io.github.instakiller.utils.Utility.solo

class AccountModule(private val igClient: IGClient) : BaseModule(Menus.accountMenu) {

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
        val newBio = readSingleString("new bio: ")
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
        if (readBoolean()) UserModule.showFullUserInfo(userInfo)
    }
}