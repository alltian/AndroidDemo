package stan.androiddemo.Media.OpenCV

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.MediaMetadataRetriever
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_string_image.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import stan.androiddemo.R
import java.util.*
import kotlin.concurrent.timer

class StringImageActivity : AppCompatActivity() {
    
    val strs = arrayListOf("$", "@", "B", "%", "8", "&", "W", "M", "#", "*", "o", "a", "h", "k", "b", "d", "p", "q", "w", "m", "Z", "0", "o", "Q", "L", "C", "J", "U", "Y", "X", "z", "c", "v", "u", "n", "x", "r", "j", "f", "t", "/", "\\", "|", "(", ")", "1", "{", "}", "[", "]", "?", "-", "_", "+", "~", "<", ">", "i", "!", "l", "I", ";", ":", ",", "\"", "^", "", "'", ".", " ")


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_string_image)
        setSupportActionBar(toolbar)
        loadOpenCV()
        title = ""

        txt_image.typeface = Typeface.createFromAsset(assets,"MSYHMONO.ttf")




        btn_open_image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"图片选择..."),0x0001)
        }

        btn_open_video.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"视频选择..."),0x0002)
        }

        btn_change_image.setOnClickListener {
            img_operate.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(img_operate.drawingCache)
            img_operate.isDrawingCacheEnabled = false

            val src = Mat()

            val dst = Mat()
            Utils.bitmapToMat(bitmap,src)
            val height = 67.0 * src.height().toDouble()   / src.width().toDouble()
            Imgproc.resize(src,src, Size(67.0,height))
            Imgproc.cvtColor(src,dst, Imgproc.COLOR_BGRA2GRAY)
//            Utils.matToBitmap(dst,bitmap)
//            img_operate.setImageBitmap(bitmap)
            src.release()
            val row = dst.rows()
            val col = dst.cols()

            val arrStr = arrayListOf<String>()


            for (i in 0 until row){
                val item = arrayListOf<String>()
                for (j in 0 until col){
                    val gray = dst.get(i,j)[0]
                    val percent = gray / 255.0
                    var count = ((strs.count() - 1) * percent).toInt()
                    item.add(strs[count])

                }
                arrStr.add(item.joinToString(" "))
            }
            dst.release()
            var result = arrStr.joinToString("\n")
            txt_image.text = result
            dst.release()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x0001){
            if (resultCode == Activity.RESULT_OK){
                if (data != null){
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,data!!.data)
                    img_operate.setImageBitmap(bitmap)
                    img_operate.visibility = View.VISIBLE
                    video_view.visibility = View.GONE
                }
            }
        }
        else if (requestCode == 0x0002){
            if (resultCode == Activity.RESULT_OK){
                if (data != null){
                  val url =  MediaStore.Video.Media.getContentUri(data.data.toString())
                    img_operate.visibility = View.GONE
                    video_view.visibility = View.VISIBLE
                    //video_view.setVideoURI(url)
                    video_view.setVideoPath(data.data.toString())
                    val mediaController = MediaController(this)
                    video_view.setMediaController(mediaController)
                    video_view.requestFocus()
                    video_view.start()
                    val mmr = MediaMetadataRetriever()
                    mmr.setDataSource(data.data.toString())


                }
            }
        }

    }

    private fun loadOpenCV(){
        val success = OpenCVLoader.initDebug()
        if (success){
            Log.i("cvtag","OpenCV Libraries loaded...")
        }
        else{
            Toast.makeText(this.applicationContext,"Warning:Could not load the OpenCV Libraries", Toast.LENGTH_SHORT).show()
        }
    }
}
