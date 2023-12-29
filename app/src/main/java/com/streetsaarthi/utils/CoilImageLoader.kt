package com.streetsaarthi.controller

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.streetsaarthi.screens.interfaces.ImageLoader
import com.streetsaarthi.utils.loadImage

class CoilImageLoader : ImageLoader {
    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        view.loadImage(url = { uri.path ?: "" })
    }
}