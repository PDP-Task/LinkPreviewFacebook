package uz.context.facebooktimeline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.nodes.Document
import uz.context.facebooktimeline.R
import uz.context.facebooktimeline.databinding.ActivityCreatePostBinding
import uz.context.facebooktimeline.util.Utils

class CreatePostActivity : AppCompatActivity() {
    private lateinit var bin: ActivityCreatePostBinding
    var imageValue: String = ""
    var titleValue: String = ""
    var desValue: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(bin.root)

        initViews()

    }

    private fun initViews() {
        bin.btnCloseEdit.setOnClickListener {
            bin.editLink.setText("")
        }
        bin.editLink.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getImage(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        bin.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun getImage(text: String) {
        if (!Patterns.WEB_URL.matcher(text).matches()) {
            bin.editLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.red)
            bin.btnPost.isEnabled = false
        } else {
            bin.btnPost.isEnabled = true
            bin.editLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.green)
            bin.progressBar.isVisible = true
            Utils().getJsoupContent(text)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Document> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(t: Document) {
                        if (t != null) {
                            val metaTags = t.getElementsByTag("meta")
                            for (element in metaTags) {
                                when {
                                    element.attr("property") == "og:image" -> {
                                        imageValue = "content"
                                        Picasso.get()
                                            .load(element.attr(imageValue))
                                            .into(bin.imageLink)
                                    }
                                    element.attr("name") == "title" -> {
                                        titleValue = "content"
                                        bin.textTitle.text =
                                            element.attr(titleValue)
                                    }
                                    element.attr("name") == "description" -> {
                                        desValue = "content"
                                        bin.textDes.text =
                                            element.attr(desValue)
                                    }
                                }
                                bin.progressBar.isVisible = false
                                bin.linearPreview.isVisible = true
                                bin.btnCloseImg.setOnClickListener {
                                    bin.editLayout.boxStrokeColor = ContextCompat.getColor(this@CreatePostActivity, R.color.purple_500)
                                    bin.linearPreview.isVisible = false
                                    bin.editLink.setText("")
                                }
                                bin.btnPost.setOnClickListener {
                                    val intent = Intent(this@CreatePostActivity,MainActivity::class.java)
                                    val title = bin.textTitle.text.toString().trim()
                                    val des = bin.textDes.text.toString().trim()
                                    intent.putExtra("url",text)
                                    intent.putExtra("title",title)
                                    intent.putExtra("des",des)
                                    setResult(RESULT_OK,intent)
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@CreatePostActivity,
                                "Result is null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(
                            this@CreatePostActivity,
                            "Enter link correctly!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onComplete() {}
                })
        }
    }
}