package io.github.instakiller.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.reel.ReelImageMedia
import com.github.instagram4j.instagram4j.models.media.reel.ReelMedia
import com.github.instagram4j.instagram4j.models.media.reel.ReelVideoMedia
import com.github.instagram4j.instagram4j.models.media.timeline.Comment
import com.github.instagram4j.instagram4j.responses.feed.FeedUserStoryResponse
import io.github.instakiller.Dyad
import io.github.instakiller.console.ConsoleHelper.readMultipleStrings
import io.github.instakiller.helpers.Downloader
import io.github.instakiller.helpers.LoggerHelper.loading
import io.github.instakiller.helpers.LoggerHelper.progress
import io.github.instakiller.helpers.UserHelper
import io.github.instakiller.utils.Constants
import io.github.instakiller.utils.Constants.errorStyle
import io.github.instakiller.utils.Constants.resultStyle
import io.github.instakiller.utils.Constants.ter
import io.github.instakiller.utils.Menus
import io.github.instakiller.utils.Utility.actionPair
import io.github.instakiller.utils.Utility.getName
import io.github.instakiller.utils.Utility.solo

class StoryModule(private val igClient: IGClient) : BaseModule(Menus.storyMenu) {

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
        val usernames = readMultipleStrings("username")
        usernames.forEach { username ->
            loading { stopLoading ->
                userHelper.getPk(username).solo({ pk ->
                    stopLoading()
                    getUserStory(pk, username)
                }, { stopLoading() })
            }
        }
    }

    private fun getUserStory(pk: Long, username: String) {
        progress { stopProgress ->
            getUserStories(pk).solo({ stories ->
                stopProgress()
                if (stories.reel != null && stories.reel.items != null) {
                    saveSingleUserStories(stories.reel.items, username)
                } else ter.println(errorStyle("No stories found for $username"))
            }, { stopProgress() })
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
            if (it is ReelVideoMedia) saveVideoStory(it, username)
        }
        ter.println(Constants.infoStyle("Bulk Download of ($username) stories ended!"))
    }

    private fun saveImageStory(
        media: ReelImageMedia,
        username: String
    ) {
        val url = media.image_versions2.candidates.first().url
        val name = url.getName()
        progress { progressDone ->
            downloader.download(url, "images/$username/stories/$name").solo({
                progressDone()
                ter.println(resultStyle("Saved $name to images/$username/stories/$name"))
            }, { progressDone() })
        }
    }

    private fun saveVideoStory(
        media: ReelVideoMedia,
        username: String
    ) {
        val url = media.video_versions.first().url
        val name = url.getName()
        progress { progressDone ->
            downloader.download(url, "videos/$username/stories/$name").solo({
                progressDone()
                ter.println(resultStyle("Saved $name to videos/$username/stories/$name"))
            }, { progressDone() })
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