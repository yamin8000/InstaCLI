package io.github.yamin8000.modules

import com.github.instagram4j.instagram4j.IGClient
import io.github.yamin8000.modules.Menus.mainMenu
import java.util.*

class MainModule(scanner: Scanner, private val igClient: IGClient) : Module(scanner, mainMenu) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> UserModule(scanner, igClient).run()
            3 -> PostModule(scanner, igClient).run()
        }

        run()
        return 0
    }
}