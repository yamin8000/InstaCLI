package io.github.instakiller.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.Comment
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.responses.live.LiveCreateResponse
import io.github.instakiller.console.ConsoleHelper.pressEnterToContinue
import io.github.instakiller.console.ConsoleHelper.readSingleString
import io.github.instakiller.helpers.LiveStreamHelper
import io.github.instakiller.helpers.LoggerHelper.loading
import io.github.instakiller.utils.Constants.askStyle
import io.github.instakiller.utils.Constants.errorStyle
import io.github.instakiller.utils.Constants.resultStyle
import io.github.instakiller.utils.Constants.ter
import io.github.instakiller.utils.Constants.warningStyle
import io.github.instakiller.utils.Menus
import io.github.instakiller.utils.Utility.solo

class LiveStreamModule(private val igClient: IGClient) : BaseModule(Menus.livestreamMenu) {

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
        pressEnterToContinue()

        run()
        return 0
    }

    private fun showLiveStreamComments() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                helper.getComments(it.broadcast_id).solo({ comments ->
                    stopLoading()
                    printComments(comments)
                }, { stopLoading() })
            }
        }
    }

    private fun printComments(comments: MutableList<Comment>) {
        comments.forEach {
            ter.println(resultStyle("${it.user.username} : ") + askStyle(it.text))
        }
    }

    private fun addCommentToLiveStream() {
        val comment = readSingleString("your comment")
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                helper.addComment(it.broadcast_id, comment).solo({
                    stopLoading()
                    ter.println(resultStyle("Comment added!"))
                }, { stopLoading() })
            }
        }
    }

    private fun showLiveStreamViewerList() {
        loading { stopLoading ->
            requireCreatedLive()
            liveStream?.let {
                helper.getViewerList(it.broadcast_id).solo({ viewerList ->
                    if (viewerList.isNotEmpty()) printViewerList(viewerList)
                    else ter.println(warningStyle("No viewer found!"))
                }, { stopLoading() })
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
            liveStream?.let {
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