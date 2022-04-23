package io.github.instakiller.modules

import com.github.instagram4j.instagram4j.IGClient
import io.github.instakiller.utils.Menus.mainMenu

class MainModule(private val igClient: IGClient) : BaseModule(mainMenu) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> UserModule(igClient).run()
            3 -> PostModule(igClient).run()
            4 -> FriendsModule(igClient).run()
            6 -> DirectMessageModule(igClient).run()
            7 -> LiveStreamModule(igClient).run()
            8 -> AccountModule(igClient).run()
            9 -> StoryModule(igClient).run()
            10 -> SettingsModule().run()
        }

        run()
        return 0
    }
}