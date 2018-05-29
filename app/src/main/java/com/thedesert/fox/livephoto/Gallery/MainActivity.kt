package com.thedesert.fox.livephoto.Gallery

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

    private fun openLivePhoto(photo:String){
        val thumb = File(photo)
        val dir = thumb.parentFile.parentFile.absolutePath
        val gif = dir + '/' +
                thumb.name.substring(0, thumb.name.lastIndexOf(".")) +
                ".gif"

        val intent = Intent(this@MainActivity, GifActivity::class.java)
        intent.putExtra("gif", gif)
        startActivity(intent)
    }

    private fun deleteLivePhoto(photo:String){
        val thumb_file = File(photo)
        val dir = thumb_file.parentFile.parentFile.absolutePath
        val gif = File(dir + '/' +
                thumb_file.name.substring(0, thumb_file.name.lastIndexOf(".")) +
                ".gif")
        var deleted = thumb_file.delete()
        deleted = gif.delete()
        Toast.makeText(this, "Livephoto deleted.", Toast.LENGTH_SHORT).show()
        initGridview()
    }

    private fun shareLivePhoto(photo:String){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND

        val thumb_file = File(photo)
        val dir = thumb_file.parentFile.parentFile.absolutePath
        val gif = File(dir + '/' +
                thumb_file.name.substring(0, thumb_file.name.lastIndexOf(".")) +
                ".gif")
        val photoUri = Uri.fromFile(gif)

        shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri)
        shareIntent.type = "image/*"
        startActivity(Intent.createChooser(shareIntent, "Send to"))
    }

    private fun initGridview(){
        val gridView = findViewById<GridView>(R.id.gridview)
        val imageAdapter = ImageAdapter(this)
        gridView.adapter = imageAdapter
        val asyncTaskLoadFiles = AsyncTaskLoadFiles(imageAdapter)
        asyncTaskLoadFiles.execute()

        gridView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parrent: AdapterView<*>?,
                                     view: View?,
                                     position: Int,
                                     id: Long) {
                val photo = parrent!!.getItemAtPosition(position).toString()
                openLivePhoto(photo)
            }
        }

        gridView.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parrent: AdapterView<*>?,
                                         view: View?,
                                         position: Int,
                                         id: Long): Boolean {
                val options = resources.getStringArray(R.array.image_options)
                val builder = AlertDialog.Builder(this@MainActivity)

                builder
                        .setTitle("Image options")
                        .setItems(options,
                                {
                                    dialog, which ->
                                    val selectedPhoto = parrent!!
                                            .getItemAtPosition(position)
                                            .toString()
                                    when (options[which]){
                                        "View" -> openLivePhoto(selectedPhoto)
                                        "Share" -> shareLivePhoto(selectedPhoto)
                                        "Delete" -> deleteLivePhoto(selectedPhoto)

                                    }
                                })
                builder.show()
                return true
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
