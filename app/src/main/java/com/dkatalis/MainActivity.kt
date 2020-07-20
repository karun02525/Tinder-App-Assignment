package com.dkatalis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.dkatalis.adapter.CardStackAdapter
import com.dkatalis.api.RestAPI
import com.dkatalis.api.RestAdapter
import com.dkatalis.api.callback.UserCallback
import com.dkatalis.model.User
import com.dkatalis.room.RoomDb
import com.dkatalis.utils.UserDiffCallback
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), CardStackListener {
    private val TAG: String = "MainActivity"

    private var api: RestAPI? = null
    private lateinit var db: RoomDb
    private var disposable: CompositeDisposable? = null

    private lateinit var userList: MutableList<User>
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter(userList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize views
        initViews()
    }

    private fun initViews() {
        userList = mutableListOf()

        disposable = CompositeDisposable()
        api = RestAdapter.getInstance()
        db = RoomDb.getDatabase(this)

        getUser()

        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollVertical(false)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.action_favorite) {
            startActivity(Intent(this, FavoriteListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUser() {
        disposable!!.add(
            api!!.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoader() }
                .doFinally { hideLoader() }
                .subscribe({ handleSuccess(it) }) { handleError(it) }
        )
    }

    private fun handleSuccess(data: UserCallback) {
        viewHideShow(false)
        Log.d(TAG, "handleSuccess ${data.results.size} ")
        if (data.results.isNotEmpty()) {
            val old = userList
            val new = mutableListOf<User>().apply {
                addAll(old)
                add(data.results[0].user)
            }
            val callback =
                UserDiffCallback(old, new)
            val result = DiffUtil.calculateDiff(callback)
            userList.add(data.results[0].user)
            adapter.setUsers(userList)
            result.dispatchUpdatesTo(adapter)
            //cardStackView.adapter?.notifyDataSetChanged()
            Log.d(TAG, "userList size is ===> ${userList.size}")
        }
    }

    private fun updateDb() {
        val user = userList[manager.topPosition-1]
        Log.d(TAG, "Fvt user is $user")
        return db.userDao().addFav(user)
    }

    private fun saveUser() {
        disposable!!.add(
            Single.create<Unit> { e -> e.onSuccess(updateDb()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "Database updated successfully ")
                }) { handleError(it) }
        )
    }

    private fun handleError(t: Throwable?) {
        viewHideShow(true)
        Toast.makeText(this, "No internet connection please try again ", Toast.LENGTH_SHORT).show()
    }

    private fun showLoader() {
        loader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loader.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called from Home")
        disposable?.clear()
    }

    private fun viewHideShow(flag:Boolean){
        if(flag){
            tv_offline.visibility=View.VISIBLE
            textView.visibility=View.VISIBLE
            relativeLayout.visibility=View.GONE

            tv_offline.setOnClickListener {   startActivity(Intent(this, FavoriteListActivity::class.java)) }

        }else{
            tv_offline.visibility=View.GONE
            textView.visibility=View.GONE
            relativeLayout.visibility=View.VISIBLE
        }
    }


    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d(TAG, "onCardDisappeared $position")
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d(TAG, "onCardSwiped $direction $userList")
        if (manager.topPosition <= adapter.itemCount) {
            getUser()
        }
        if (direction?.name == "Right"){
            saveUser()
        }
    }

    override fun onCardCanceled() {
        Log.d(TAG, "onCardCanceled")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.d(TAG, "onCardAppeared $position and adapter size is ${adapter.itemCount}")
    }

    override fun onCardRewound() {

    }
}
