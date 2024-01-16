package com.streetsaarthi.nasvi.screens.main.dashboard

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.models.mix.ItemAds
import com.streetsaarthi.nasvi.screens.main.webPage.WebPage
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.loadImage

class BannerViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private var inflater: LayoutInflater? = null
    private val images = arrayOf(R.drawable.background, R.drawable.background, R.drawable.background, R.drawable.background)
    var itemMain : ArrayList<ItemAds> ?= ArrayList()


    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getCount(): Int {
        return itemMain!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.item_banner, null)
        var bannerItem = view.findViewById<AppCompatImageView>(R.id.bannerItem)
//        bannerItem.setImageResource(images[0])
        bannerItem.loadImage(url = { itemMain?.get(position)!!.ad_media })
        view.setOnClickListener {
            Handler(Looper.getMainLooper()).post(Thread {
                MainActivity.activity.get()?.runOnUiThread {
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(itemMain?.get(position)!!.ad_link)
                    )
                    try {
                        context.startActivity(webIntent)
                    } catch (ex: ActivityNotFoundException) {
                    }
                }
            })
        }
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

    fun submitData(itemAds: ArrayList<ItemAds>) {
        itemMain!!.clear()
        itemMain!!.addAll(itemAds)
    }
}