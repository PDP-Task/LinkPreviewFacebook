package uz.context.facebooktimeline.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.nodes.Document
import uz.context.facebooktimeline.R
import uz.context.facebooktimeline.adapter.PostAdapter
import uz.context.facebooktimeline.adapter.StoryAdapter
import uz.context.facebooktimeline.databinding.ActivityMainBinding
import uz.context.facebooktimeline.model.Post
import uz.context.facebooktimeline.model.Story
import uz.context.facebooktimeline.util.Utils
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter
    private lateinit var bin: ActivityMainBinding
    var imageValue: String? = null
    var titleValue: String? = null
    var desValue: String? = null
    private val storyItems: ArrayList<Story> = ArrayList()
    private val postItems: ArrayList<Post> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bin.root)

        initViews()
    }


    private var forResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageValue = data!!.getStringExtra("url")
            titleValue = data.getStringExtra("title")
            desValue = data.getStringExtra("des")
            Utils().getJsoupContent(imageValue!!)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Document> {
                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(t: Document) {
                        if (t != null) {
                            val metaTags = t.getElementsByTag("meta")
                            for (element in metaTags) {
                                if (element.attr("property") == "og:image") {
                                    imageValue = "content"
                                    postAdapter.addItem(
                                        Post(
                                            element.attr("content"),
                                            titleValue!!,
                                            desValue!!
                                        )
                                    )
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Result is null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            "Enter link correctly!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onComplete() {}
                })
        }
    }

    private fun initViews() {

        storyAdapter = StoryAdapter(items())
        bin.recyclerHor.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }
        postAdapter = PostAdapter(this, postItems)
        bin.recyclerVer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        bin.recyclerVer.adapter = postAdapter


        bin.includedLayout.linearMind.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            forResult.launch(intent)
        }
    }

    private fun items(): ArrayList<Story> {
        storyItems.add(Story(R.drawable.sa, "Sakura 1"))
        storyItems.add(Story(R.drawable.sak, "Sakura 2"))
        storyItems.add(Story(R.drawable.sakjo, "Sakura 3"))
        storyItems.add(Story(R.drawable.sakura, "Sakura 4"))
        storyItems.add(Story(R.drawable.sakurag, "Sakura 5"))
        return storyItems
    }
}
//https://www.marvel.com/characters/spider-man-peter-parker
//https://www.marvel.com/characters/vision
//https://www.marvel.com/
//https://www.marvel.com/characters/green-goblin-norman-osborn