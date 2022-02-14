package io.github.yamin8000.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.reel.ReelImageMedia
import com.github.instagram4j.instagram4j.models.media.reel.ReelMedia
import com.github.instagram4j.instagram4j.models.media.timeline.Comment
import com.github.instagram4j.instagram4j.responses.feed.FeedUserStoryResponse
import io.github.yamin8000.Dyad
import io.github.yamin8000.console.ConsoleHelper.getMultipleStrings
import io.github.yamin8000.helpers.Downloader
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.helpers.LoggerHelper.progress
import io.github.yamin8000.helpers.UserHelper
import io.github.yamin8000.utils.Constants
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Menus
import io.github.yamin8000.utils.Utility.actionPair
import io.github.yamin8000.utils.Utility.solo
import java.util.*

class StoryModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, Menus.storyMenu) {

    private val downloader = Downloader(igClient.httpClient)
    private val userHelper = UserHelper(igClient)

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showCurrentUserStories()
            3 -> saveUsersStories()
        }

        run()
        return 0
    }

    private fun showCurrentUserStories() {
        TODO("Not yet implemented")
    }

    private fun saveUsersStories() {
        val usernames = scanner.getMultipleStrings("username")
        usernames.forEach { username ->
            loading { stopLoading ->
                userHelper.getPk(username).solo { pk ->
                    stopLoading()
                    progress {
                        getUserStories(pk).solo { stories ->
                            it()
                            if (stories.reel != null && stories.reel.items != null) {
                                saveSingleUserStories(stories.reel.items, username)
                            } else ter.println(errorStyle("No stories found for $username"))
                        }
                    }
                }
            }
        }
    }

    private fun saveSingleUserStories(
        items: List<ReelMedia>,
        username: String
    ) {
        ter.println(Constants.infoStyle("Starting download of ($username) stories bulk download!"))
        items.forEach {
            handleStoryCaption(it.caption)
            if (it is ReelImageMedia) saveImageStory(it, username)
        }
        ter.println(Constants.infoStyle("Bulk Download of ($username) stories ended!"))
    }

    private fun saveImageStory(
        it: ReelImageMedia,
        username: String
    ) {

        val url = it.image_versions2.candidates.first().url
        val name = url.substringAfterLast("/").substringBefore("?")
        progress { progressDone ->
            downloader.download(url, "images/$username/stories/$name").solo {
                progressDone()
                ter.println(resultStyle("Saved $name to images/$username/stories/$name"))
            }
            progressDone()
        }
    }

    private fun handleStoryCaption(caption: Comment.Caption?) {
        if (caption != null)
            ter.println(resultStyle("Story Caption: ${caption.text}"))
    }

    private fun getUserStories(pk: Long): Dyad<FeedUserStoryResponse?> {
        return igClient.actions
            .story()
            .userStory(pk)
            .actionPair()
    }
}