package com.photoeditorsdk.android.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic
import ly.img.android.pesdk.assets.font.basic.FontPackBasic
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.constant.Directory
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.SaveSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.*
import ly.img.android.pesdk.ui.panels.item.PersonalStickerAddItem
import ly.img.android.pesdk.ui.utils.PermissionRequest
import ly.img.android.serializer._3.IMGLYFileWriter
import java.io.File
import java.io.IOException

class MainActivity : Activity(), View.OnClickListener {
    var menuflag = 0;

    companion object {
        const val PESDK_RESULT = 1
        /*const val EDIT_RESULT = 2*/
        const val CREATE_RESULT = 3
        const val PLAY_RESULT = 4
        const val SETTINGS_RESULT = 5
    }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView();
    }

    @SuppressLint("WrongViewCast")
    private fun initView(){

    /*activity_main 버튼 연결*/

        val create = findViewById<ImageButton>(R.id.create_btn)
        val play = findViewById<ImageButton>(R.id.play_btn)
        /*val edit = findViewById<ImageButton>(R.id.edit_btn)*/
        val settings = findViewById<ImageButton>(R.id.settings_btn)

        create.setOnClickListener(this)
        play.setOnClickListener(this)
        /*edit.setOnClickListener(this)*/
        settings.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.create_btn -> {
                menuflag = CREATE_RESULT
                selectMenu()}
            R.id.play_btn -> {
                menuflag = PLAY_RESULT
                selectMenu()}
            /*R.id.edit_btn -> {
                menuflag = EDIT_RESULT
                selectMenu()}*/
            R.id.settings_btn -> {
                menuflag = SETTINGS_RESULT
                selectMenu()}
        }
    }

    fun selectMenu(){
        when (menuflag){
            CREATE_RESULT -> {
            val intent1 = Intent(this, CallGallery::class.java)
            intent1.putExtra("createCode", CREATE_RESULT)
            }
            PLAY_RESULT -> {
                val intent2 = Intent(this, CallGallery::class.java)
                intent2.putExtra("playCode", PLAY_RESULT)
            }
            /*EDIT_RESULT -> {
                val intent3 = Intent(this, CallGallery::class.java)
                intent3.putExtra("editCode", EDIT_RESULT)
            }*/
            SETTINGS_RESULT -> {
                val intent4 = Intent(this, SettingsSystem::class.java)
                intent4.putExtra("settingsCode", SETTINGS_RESULT)
            }
        }
    }

}
