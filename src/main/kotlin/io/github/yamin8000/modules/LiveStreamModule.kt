package io.github.yamin8000.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.Comment
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.responses.live.LiveCreateResponse
import io.github.yamin8000.console.ConsoleHelper.getSingleString
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.helpers.LiveStreamHelper
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.utils.Constants.askStyle
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Constants.warningStyle
import io.github.yamin8000.utils.Menus
import java.util.*

class LiveStreamModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, Menus.livestreamMenu) {

    private val helper by lazy(LazyThreadSafetyMode.NONE) { LiveStreamHelper(igClient) }

    private var liveStream: LiveCreateResponse? = null

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> createLiveStream()
            3 -> startLiveStream()
            4 -> stopLiveStream()
            5 -> showLiveStreamInfo()
            6 -> showLiveStreamViewerCount()
            7 -> showLiveStreamViewerList()
            8 -> addCommentToLiveStream()
            9 -> showLiveStreamComments()
            else -> {
                ter.println(errorStyle("Invalid input, try again!"))
                run()
            }
        }
        scanner.pressEnterToContinue()

        run()
        return 0
    }

    private fun showLiveStreamComments() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                val (comments, error) = helper.getComments(it.broadcast_id)
                stopLoading()
                if (comments != null && error == null) printComments(comments)
                else ter.println(errorStyle("Failed to get comments: ${error?.message}"))
            }
            stopLoading()
        }
    }

    private fun printComments(comments: MutableList<Comment>) {
        comments.forEach {
            ter.println(resultStyle("${it.user.username} : ") + askStyle(it.text))
        }
    }

    private fun addCommentToLiveStream() {
        val comment = scanner.getSingleString("your comment")
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                val (response, error) = helper.addComment(it.broadcast_id, comment)
                stopLoading()
                if (response != null && error == null) {
                    ter.println(resultStyle("Comment added!"))
                } else ter.println(errorStyle("Failed to add comment to live stream: ${error?.message}"))
            }
        }
    }

    private fun showLiveStreamViewerList() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                val (viewerList, error) = helper.getViewerList(it.broadcast_id)
                stopLoading()
                if (viewerList != null && error == null) {
                    if (viewerList.isNotEmpty()) printViewerList(viewerList)
                    else ter.println(warningStyle("No viewer found!"))
                } else ter.println(errorStyle("Failed to get viewer list: ${error?.message}"))
            }
            stopLoading()
        }
    }

    private fun printViewerList(viewerList: MutableList<Profile>) {
        viewerList.forEachIndexed { index, user ->
            ter.println(resultStyle("${index + 1}. ${user.username} (${user.full_name})"))
        }
    }

    private fun showLiveStreamViewerCount() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let { it ->
                val (liveInfo, error) = helper.getLiveInfo(it.broadcast_id)
                stopLoading()
                if (liveInfo != null && error == null) {
                    val viewerCount = liveInfo.extraProperties
                        .entries
                        .find { entry -> entry.key == "viewer_count" }
                        ?.value
                        .toString()
                    ter.println(resultStyle("($viewerCount) people are watching this live stream"))
                } else ter.println(errorStyle("Failed to get live stream viewer count: ${error?.message}"))
            }
            stopLoading()
        }
    }

    private fun showLiveStreamInfo() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                val (liveInfo, error) = helper.getLiveInfo(it.broadcast_id)
                stopLoading()
                if (liveInfo != null && error == null) {
                    val info = liveInfo.extraProperties.map { pair -> "${pair.key} : ${pair.value}" }
                    ter.println(resultStyle(info.joinToString("\n")))
                } else ter.println(errorStyle("Failed to get live info: ${error?.message}"))
            }
            stopLoading()
        }
    }

    private fun stopLiveStream() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                val (stoppedLive, error) = helper.stopLive(it.broadcast_id)
                stopLoading()
                if (stoppedLive != null && error == null) ter.println(resultStyle("Live stream stopped successfully!"))
                else ter.println(errorStyle("Failed to stop live stream: ${error?.message}"))
            }
            stopLoading()
        }
    }

    private fun startLiveStream() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                val (startedLive, error) = helper.startLive(it.broadcast_id)
                stopLoading()
                if (startedLive != null && error == null) ter.println(resultStyle("Started live stream!"))
                else ter.println(errorStyle("Failed to start live stream: ${error?.message}"))
            }
            stopLoading()
        }
    }

    private fun requireCreatedLive() {
        if (liveStream == null) ter.println(errorStyle("You have to create a live stream first!"))
    }

    private fun createLiveStream() {
        loading { stopLoading ->
            val (live, error) = helper.createLive()
            stopLoading()
            if (live != null && error == null) {
                liveStream = live
                ter.println(resultStyle("Live stream created successfully!"))
                ter.println(resultStyle("Enter url and key to your streaming platform (OBS Studio), then start streaming (both here and there)!"))
                ter.println(resultStyle("Live stream broadcast id: ${live.broadcast_id}"))
                ter.println(resultStyle("Live stream broadcast url: ${live.broadcastUrl}"))
                ter.println(resultStyle("Live stream broadcast key: ${live.broadcastKey}"))
            } else ter.println(errorStyle("Failed to create live stream: ${error?.message}"))
        }
    }
}