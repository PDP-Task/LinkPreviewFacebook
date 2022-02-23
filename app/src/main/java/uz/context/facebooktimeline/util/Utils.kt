package uz.context.facebooktimeline.util

import io.reactivex.rxjava3.core.Observable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class Utils {
    fun getJsoupContent(url: String): Observable<Document> {
        return Observable.fromCallable {
            try {
                return@fromCallable Jsoup.connect(url).timeout(0)
                    .get()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}