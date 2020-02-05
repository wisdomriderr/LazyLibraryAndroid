package org.wisdomrider.lazylibrarydemo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog.view.*
import org.wisdomrider.lazylibrary.LazyBase
import org.wisdomrider.lazylibrary.LazyRecyclerAdapter
import org.wisdomrider.lazylibrary.LazyViewHolder

class MainActivity : LazyBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var myApi = application as App


        // Broadcast Receive Example
        lazy.receiveBroadcast {
        var family =  it.extras.getSerializable("data") as Family
           Toast.makeText(this, family.name, Toast.LENGTH_LONG).show()
        }

        var family = Family("Radhika Neupane", "mother")
        family.sendToBroadcast()

        // Fetching data from Api And loading on Recycler view
        myApi.api.a().fetch(
            {
                if (it.code() == 200) {
                    recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                    recycler.adapter = LazyRecyclerAdapter(
                        R.layout.dialog, object : LazyViewHolder {
                            override fun lazyOnBindViewHolder(
                                holder: LazyRecyclerAdapter.WisdomHolder,
                                list: List<Any?>,
                                position: Int
                            ) {
                                var mylist = list as ArrayList<Family>
                                var textView = holder.itemView.name
                                textView.text = mylist[position].name
                            }
                        }, it.body()!!.family
                    )
                }
            }, { error ->
                Log.e("Error", error.message)
            }, true, fetchData = false
        )

        // Adding A token Interceptor and fetching data

        myApi.lazyInterceptor(
            "Authorization",
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjcyYTQwZTI1ZmRmMTk1ZjJjOWVlZjIxYjU0NGUzYWFlMTFjMmZhMjE5MTk5ZWVkYjA1Njg4Nzk0N2RlOTdhMzlhYjFmZTYzNmQ5MzUyNWEzIn0.eyJhdWQiOiIxIiwianRpIjoiNzJhNDBlMjVmZGYxOTVmMmM5ZWVmMjFiNTQ0ZTNhYWUxMWMyZmEyMTkxOTllZWRiMDU2ODg3OTQ3ZGU5N2EzOWFiMWZlNjM2ZDkzNTI1YTMiLCJpYXQiOjE1NzY2NTA3NzksIm5iZiI6MTU3NjY1MDc3OSwiZXhwIjoxNjA4MjczMTc5LCJzdWIiOiI0MiIsInNjb3BlcyI6W119.hHFj6I5tqjZAgY60QVvaFbWarU--tPWeAInhbG4ntFdkeI1j0b0GuQ5gYq5bN5SCFntt6eoQdcQyywKxSKL9eUea0iR03V7PP5ebxlFKAyf5cQ_v58kF2CLxTYwfzUpZUL8Bz8J3Et97gPb8IcZAJKlFy9V5twE6HrehaaSDOvYRHYVUeusQwm0IH-gBwsl3_RQ6q8h1tPrLYYAFC2HSbFPOSQf29DPW-63WlAqHQ_Vt81RaLfiNxlNfDhPtXh1T-7VoPAweLvRxZCbbFFNcKf0U8yXd66kNgkkMlBXk3GAIE9OhZCP08EbZVlsWes1KVcRC4AA39U6W-NndiBGWUOUJQeWwkTcftXZ6myOAaDADlMLk76vXseYEqwtvbn_oHWrhKVfTbdP3Cev94R4TfigxTHUwYB_oLJskIfj5NlHMMFAMcY6wZuVwT9aThFd_dA3wwYW8UzzJBk3pNWHDdjWIlE0fKLHjp4DvoHTOLo1kWYesL6y-wFo7J8gsu48H3-6qS0M31u_Vt25p9OLM-dYSBjyOfjxgV3Wf0RVVQzMWqtUHfgdUyfmWS7G4eQ5POfWhtnJfISaSykFrvqqx1lOY2rgdTMLed72OD5uKBAkAWLul02BuIgVcYGuVgcyF6WDxGpjUdIVMYvFFe0CLNNrGfqsbZ4tKSG9H8hO1edA"
        )

        myApi.api.data.fetch(
            {
                Toast.makeText(this, it!!.message(), Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, true, fetchData = false, progressBarTittle = "Fetching Secure REST API"
        )

         // Auto receive sms on lazyPinView
        enableOneTimeOtpCode( "PES_ALERT",lazyPingView)
    }
}