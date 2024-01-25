package com.streetsaarthi.nasvi.controller

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.streetsaarthi.nasvi.screens.interfaces.ImageLoader
import com.streetsaarthi.nasvi.utils.loadImage

class CoilImageLoader : ImageLoader {
    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        view.loadImage(url = { uri.path ?: "" })
    }
}