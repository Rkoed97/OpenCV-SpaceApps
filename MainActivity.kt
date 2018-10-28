package ro.cnmv.qube.opencv

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import org.bytedeco.javacpp.Loader
import org.bytedeco.javacv.AndroidFrameConverter
import org.bytedeco.javacv.OpenCVFrameConverter
import org.bytedeco.javacpp.Loader.sizeof
import org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours
import org.bytedeco.javacpp.opencv_core.*
import org.bytedeco.javacpp.opencv_imgproc.*


class MainActivity : Activity() {
    private lateinit var preview: CameraPreview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            throw Error("Camera permission not granted")
        }

        val frameConverter = AndroidFrameConverter()
        val converter = OpenCVFrameConverter.ToMat()
        val gray = Mat()
        val thresh = Mat()
        val contours = MatVector()

        val width = 1280
        val height = 720

        preview = CameraPreview(surfaceView, width, height,
            Camera.PreviewCallback { data, camera ->
                val frame = frameConverter.convert(data, width, height)              //
                                                                                     //
                val image = converter.convertToMat(frame)                            //  Here we process the image and make it an array of grayscale values
                                                                                     //
                cvtColor(image, gray, COLOR_BGR2GRAY)                                //

                threshold(gray, thresh, 127.0, 255.0, THRESH_BINARY)                 //
                                                                                     //
                contours.clear()                                                     //  Here we draw the contours based on the differences of the greyscale values
                findContours(thresh, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE)   //  We then Convert the image back to a bitmap and output it in the imageview(see activity_main.xml)
                                                                                     //
                drawContours(image, contours, -1, Scalar(0.0, 255.0, 255.0, 255.0))  //

                val bitmap = frameConverter.convert(frame)
                imageView.setImageBitmap(bitmap)

                camera.addCallbackBuffer(data)
            }
        )
    }
}

class CameraPreview : SurfaceHolder.Callback {
    private lateinit var camera: Camera
    private val callback: Camera.PreviewCallback
    private val previewWidth: Int
    private val previewHeight: Int

    constructor(sv: SurfaceView, w: Int, h: Int, cb: Camera.PreviewCallback) {
        val holder = sv.holder
        holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        callback = cb
        previewWidth = w
        previewHeight = h
        holder.setFixedSize(w, h)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        camera = Camera.open()
        camera.setPreviewDisplay(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val params = camera.parameters

        val picSizes = camera.parameters.supportedPreviewSizes
        picSizes.forEach{ Log.e("PicSize", "${it.width}x${it.height}") }

        val width = previewWidth
        val height = previewHeight

        params.setPreviewSize(width, height)

        with(params) {
            setPreviewSize(width, height)
            previewFormat = ImageFormat.NV21

            focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
        }

        camera.parameters = params

        camera.setPreviewCallbackWithBuffer(callback)
        val size = params.previewSize
        val data = ByteArray(size.width * size.height *
                ImageFormat.getBitsPerPixel(params.previewFormat) / 8)
        camera.addCallbackBuffer(data)

        camera.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera.stopPreview()
        camera.release()
    }
}
