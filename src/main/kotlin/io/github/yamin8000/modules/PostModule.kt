package io.github.yamin8000.modules

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.*
import io.github.yamin8000.Dyad
import io.github.yamin8000.console.ConsoleHelper.getBooleanInput
import io.github.yamin8000.console.ConsoleHelper.getIntegerInput
import io.github.yamin8000.console.ConsoleHelper.getMultipleStrings
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.helpers.Downloader
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.helpers.LoggerHelper.progress
import io.github.yamin8000.helpers.PostsHelper
import io.github.yamin8000.helpers.UserHelper
import io.github.yamin8000.utils.Constants.downloadDir
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Constants.warningStyle
import io.github.yamin8000.utils.Menus.postMenu
import io.github.yamin8000.utils.Utility.getName
import io.github.yamin8000.utils.Utility.isoTimeOfEpoch
import java.util.*

class PostModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, postMenu) {

    private val downloader: Downloader by lazy(LazyThreadSafetyMode.NONE) { Downloader(igClient.httpClient) }

    private val userHelper: UserHelper by lazy(LazyThreadSafetyMode.NONE) { UserHelper(igClient) }

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showUserPosts()
            3 -> saveUserPostsImages()
        }

        run()
        return 0
    }

    private fun showUserPosts() {
        val usernames = scanner.getMultipleStrings("username")
        usernames.forEach { username ->
            val limit = getMediaLimit(username)
            progress {
                val (posts, error) = getSingleUserPost(username, limit)
                it()
                if (posts != null && error == null) {
                    printPosts(posts)
                    val isSaving = scanner.getBooleanInput("Do you want to save posts' images as files? (y/n)")
                    if (isSaving) saveImages(posts, username)
                } else ter.println(errorStyle("Failed to get posts! Error: ${error?.message}"))
            }
        }
    }

    private fun saveImages(posts: List<TimelineMedia>, username: String) {
        posts.forEachIndexed { index, timelineMedia ->
            ter.println(TextColors.brightYellow("(${index + 1}/${posts.size}) Saving..."))
            when (timelineMedia) {
                is TimelineCarouselMedia -> saveCarouselPostImages(timelineMedia.carousel_media, username)
                is TimelineImageMedia -> saveSinglePostImage(username, timelineMedia)
                //is TimelineVideoMedia -> saveVideoPost(timelineMedia, username)
                else -> ter.println(errorStyle("Skipping, Unsupported media type! => ${timelineMedia.media_type}"))
            }
        }
        ter.println(resultStyle("All images of ($username) have been saved!"))
    }

    private fun saveCarouselPostImages(carouselItems: List<CarouselItem>, username: String) {
        carouselItems.forEach { if (it is ImageCarouselItem) saveSinglePostImage(username, it) }
    }

    private fun getSingleUserPost(username: String, limit: Int): Dyad<List<TimelineMedia>?> {
        val (posts, error) = PostsHelper(igClient).getUserFeed(username, limit)
        return if (posts != null && error == null) {
            if (posts.isNotEmpty()) posts to null
            else null to Exception("($username) has no posts!")
        } else null to Exception("Failed to get posts! Error: ${error?.message}")
    }

    private fun saveSinglePostImage(username: String, media: Any) {
        val imageUrl = getImageUrl(media)
        if (imageUrl != null) {
            val imageName = imageUrl.getName()
            val filePath = "images/$username/posts/$imageName"
            progress {
                val (imageFile, error) = downloader.download(imageUrl, filePath)
                it()
                if (imageFile != null && error == null) {
                    ter.println(resultStyle("($username) -> Image saved successfully to $$downloadDir/images/$username/posts/$imageName"))
                } else ter.println(errorStyle("Skipping, Failed to download image! Error: ${error?.message}"))
            }
        } else ter.println(warningStyle("Skipping, ($username) -> Image url is null!"))
    }

    private fun getImageUrl(media: Any): String? {
        return when (media) {
            is ImageCarouselItem -> media.image_versions2.candidates.first().url
            is TimelineImageMedia -> media.image_versions2.candidates.first().url
            else -> null
        }
    }

    private fun saveUserPostsImages() {
        val usernames = scanner.getMultipleStrings("username")
        usernames.forEach { username ->
            val limit = getMediaLimit(username)
            progress {
                val (posts, error) = getSingleUserPost(username, limit)
                it()
                if (posts != null && error == null) saveImages(posts, username)
                else ter.println(errorStyle("Failed to get posts! Error: ${error?.message}"))
            }
        }
    }

    private fun getMediaLimit(username: String): Int {
        return loading {
            val (user, userError) = userHelper.getUserInfoByUsername(username)
            it()
            if (user != null && userError == null) ter.println(TextColors.brightYellow("($username) has ${user.media_count} posts."))
            else ter.println(errorStyle("Skipping, Failed to get user info! Error: ${userError?.message}"))
            return@loading scanner.getIntegerInput("Enter number of posts to fetch:", 0 until Integer.MAX_VALUE)
        }
    }

    private fun printPosts(posts: List<TimelineMedia>) {
        posts.forEach {
            printSinglePost(it)
            scanner.pressEnterToContinue("see next post...")
        }
    }

    private fun printSinglePost(post: TimelineMedia) {
        ter.println("${resultStyle("Caption:")} ${TextStyles.bold(post.caption?.text ?: "")}")
        ter.println("${resultStyle("Date:")} ${TextStyles.bold(isoTimeOfEpoch(post.caption.created_at_utc))}")
        ter.println("${resultStyle("Likes:")} ${TextStyles.bold(post.like_count.toString())}")
        ter.println("${resultStyle("Comments:")} ${TextStyles.bold(post.comment_count.toString())}")
        ter.println("${resultStyle("Location:")} ${TextStyles.bold(post.location?.name ?: "")}")

        post.preview_comments?.let { printPreviewComments(it) }
    }

    private fun printPreviewComments(previewComments: MutableList<Comment>) {
        ter.println(resultStyle("Top Comments:"))
        previewComments.forEach { ter.println("${TextColors.blue(it.user.username)}: ${resultStyle(it.text)}") }
    }

    private fun saveVideoPost(timelineMedia: TimelineVideoMedia, username: String) {
        val videoUrl = timelineMedia.video_versions.first().url
        val videoName = videoUrl.getName()
        val filePath = "images/$username/posts/$videoName"
        progress {
            val (video, error) = downloader.download(videoUrl, filePath)
            it()
            if (video != null && error == null) ter.println(resultStyle("Saved video as $filePath"))
            else ter.println(errorStyle("Failed to save video! Error: ${error?.message}"))
        }
    }
}