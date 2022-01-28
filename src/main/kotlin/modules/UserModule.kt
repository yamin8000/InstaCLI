package yamin.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.User
import yamin.console.printlnC
import yamin.helpers.Downloader
import yamin.helpers.LoggerHelper.loading
import yamin.helpers.LoggerHelper.progress
import yamin.helpers.UserHelper
import yamin.modules.Menus.userMenu
import yamin.console.ConsoleHelper.getBooleanInput
import yamin.console.ConsoleHelper.getMultipleStrings
import yamin.utils.Constants.downloadDir
import yamin.utils.FileUtils.createDirIfNotExists
import java.util.*

class UserModule(scanner: Scanner, private val igClient: IGClient) : Module(scanner, userMenu) {

    private val downloader: Downloader by lazy(LazyThreadSafetyMode.NONE) { Downloader(igClient.httpClient) }

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showCurrentUserInfo()
            3 -> showUsersInfoByUsername()
            4 -> downloadUsersProfilePictures(scanner.getMultipleStrings("username"))
            else -> {
                printlnC { "Invalid input".red.bold }
                run()
            }
        }

        run()
        return 0
    }

    private fun downloadUsersProfilePictures(usernames: List<String>) {
        usernames.forEach { username ->
            createDirIfNotExists("images/$username/profile_pictures")
            loading { loadingDone ->
                val (user, error) = UserHelper(igClient).getUserInfoByUsername(username)
                loadingDone()
                progress { progressDone ->
                    if (user != null && error == null) downloadSingleUserProfilePicture(user, username, progressDone)
                    else {
                        printlnC { "Skipping, User with username $username not found! => ${error?.message}".red.bold }
                        progressDone()
                    }
                }
            }
        }
    }

    private fun downloadSingleUserProfilePicture(
        user: User,
        username: String,
        progressDone: () -> Unit
    ) {
        val imageUrl = user.hd_profile_pic_url_info.url
        val imageName = imageUrl.substringAfterLast("/").substringBefore("?")
        val (imageFile, downloadError) = downloader.download(imageUrl, "images/$username/profile_pictures/$imageName")
        progressDone()
        if (imageFile != null && downloadError == null) {
            printlnC { "Image saved successfully to $downloadDir/images/$username/$imageName".green.bright }
        } else printlnC { "Skipping, Failed to download image for $username => ${downloadError?.message}".red.bold }
    }

    private fun showUsersInfoByUsername() {
        val usernames = scanner.getMultipleStrings("username")
        usernames.forEach { username ->
            progress {
                val (userInfo, error) = UserHelper(igClient).getUserInfoByUsername(username)
                it()
                printUserInfo(userInfo, error)
            }
        }
    }

    private fun showCurrentUserInfo() {
        progress {
            val (userInfo, error) = UserHelper(igClient).getCurrentUserInfo()
            it()
            printUserInfo(userInfo, error)
        }
    }

    private fun printUserInfo(userInfo: User?, error: Throwable?) {
        if (userInfo != null && error == null) {
            showShortUserInfo(userInfo)
            checkIfMoreUserInfoNeeded(userInfo)
        } else printlnC { "Failed to get user info! Error: ${error?.message}".red.bold }
    }

    private fun checkIfMoreUserInfoNeeded(userInfo: User) {
        printlnC { "Do you want to see full user info? (y/n)".blue }
        if (scanner.getBooleanInput()) showFullUserInfo(userInfo)
    }

    companion object {

        fun showShortUserInfo(user: User) {
            printlnC { "User info:".green.bright }

            printlnC { "Full name: ".green + user.full_name.green.bright.bold }
            printlnC { "Username: ".green + user.username.green.bright.bold }
            printlnC { "Bio: ".green + user.biography.green.bright.bold }
            printlnC { "Media Count: ".green + user.media_count.green.bright.bold }
            printlnC { "Follower Count: ".green + user.follower_count.green.bright.bold }
            printlnC { "Following Count: ".green + user.following_count.green.bright.bold }
        }

        fun showFullUserInfo(user: User) {
            showShortUserInfo(user)
            printlnC { "Profile Url: ".green + "https://instagram.com/${user.username}".green.bright }
            printlnC { "Profile Pic Url: ".green + user.profile_pic_url.green.bright }
            printlnC { "Private or Public: ".green + (if (user.is_private) "Private" else "Public").green.bright }
            printlnC { "Verified or not (Blue tick): ".green + (if (user.is_verified) "Verified" else "Not Verified").green.bright }
            printlnC { "Business or Regular: ".green + (if (user.is_business) "Business" else "Regular").green.bright }
            printlnC { "External Url: ".green + user.external_url.green.bright }
        }
    }
}