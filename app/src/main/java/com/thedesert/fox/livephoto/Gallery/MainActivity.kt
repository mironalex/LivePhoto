package com.thedesert.fox.livephoto.Gallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.thedesert.fox.livephoto.Camera.CameraActivity
import com.thedesert.fox.livephoto.R

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private final val REQUEST_CODE = 1

    private fun requestPermissions(){
        if((ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) or
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) or
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) or
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO), REQUEST_CODE)
        }
        initGridview()
    }

    private fun createFolder(){
        val folderName = "LivePhoto/.thumbnails"
        val file = File(Environment.getExternalStorageDirectory(), folderName)
        if(!file.exists()){
            file.mkdirs()
        }
    }

    private fun initGridview(){
        val gridView = findViewById<GridView>(R.id.gridview)
        val imageAdapter = ImageAdapter(this)
        gridView.adapter = imageAdapter
        val asyncTaskLoadFiles = AsyncTaskLoadFiles(imageAdapter)
        asyncTaskLoadFiles.execute()

        gridView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parrent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val thumb = File(parrent!!.getItemAtPosition(position).toString())
                val dir = thumb.parentFile.parentFile.absolutePath
                val gif = dir + '/' +
                        thumb.name.substring(0, thumb.name.lastIndexOf(".")) +
                        ".gif"

                val intent = Intent(this@MainActivity, GifActivity::class.java)
                intent.putExtra("gif", gif)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        requestPermissions()
        createFolder()
    }

    fun openCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_create -> openCamera()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        initGridview()
    }
}
