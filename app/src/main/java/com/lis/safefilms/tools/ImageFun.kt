package com.lis.safefilms.tools

import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageFun {
    fun setImage(image: String, imageView: ImageView){
        if(image.isNotEmpty()){
            Picasso.get().load(image).into(imageView)
        }
    }
}