package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_media.*
import stan.androiddemo.Media.Audio.AudioListActivity
import stan.androiddemo.Media.Live.LiveActivity
import stan.androiddemo.Media.OpenCV.OpenCVActivity
import stan.androiddemo.Media.TakePhoto.TakePhotoActivity
import stan.androiddemo.Media.ffmpeg.FFmpegActivity

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("拍照","音频","OpenCV","FFmpeg","直播"))
        list_view_layout.adapter = adapter

        list_view_layout.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){

                "拍照"->
                {
                    val intent = Intent(this, TakePhotoActivity::class.java)
                    startActivity(intent)
                }
                "音频"->
                {
                    val intent = Intent(this, AudioListActivity::class.java)
                    startActivity(intent)
                }
                "OpenCV"->
                {
                    val intent = Intent(this, OpenCVActivity::class.java)
                    startActivity(intent)
                }
                "FFmpeg"->
                {
                    val intent = Intent(this, FFmpegActivity::class.java)
                    startActivity(intent)
                }
                "直播"->
                {
                    val intent = Intent(this, LiveActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}
