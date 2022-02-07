package io.github.yamin8000.modules

import com.github.instagram4j.instagram4j.IGClient
import io.github.yamin8000.utils.Menus.mainMenu
import java.util.*

class MainModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, mainMenu) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> UserModule(scanner, igClient).run()
            3 -> PostModule(scanner, igClient).run()
            4 -> FriendsModule(scanner, igClient).run()
            6 -> DirectMessageModule(scanner, igClient).run()
            7 -> LiveStreamModule(scanner, igClient).run()
            8 -> AccountModule(scanner, igClient).run()
            10 -> SettingsModule(scanner).run()
        }

        run()
        return 0
    }
}