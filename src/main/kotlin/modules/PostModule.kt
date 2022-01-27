package yamin.modules

import com.github.instagram4j.instagram4j.IGClient
import yamin.modules.Menus.postInfo
import java.util.*

class PostModule(scanner: Scanner, private val igClient: IGClient) : Module(scanner, postInfo) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showUserPosts()
            3 -> saveUserPosts()
        }

        run()
        return 0
    }

    private fun showUserPosts() {
        TODO("Not yet implemented")
    }

    private fun saveUserPosts() {
        TODO("Not yet implemented")
    }
}