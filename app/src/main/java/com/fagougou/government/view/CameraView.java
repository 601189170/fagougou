package com.fagougou.government.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.fagougou.government.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused", "deprecation"})
public class CameraView extends TextureView implements
     TextureView.SurfaceTextureListener, Camera.PreviewCallback
       {
    public static final String FACE_LICENSE = "idl-license.face-android";
    public static final String FACE_LICENSE_ID = "bp-face-android";




    private int mCameraId;
    private Camera.CameraInfo mCameraInfo;

    private Camera mCamera;
    private int mCameraDegree;
    private int mWidth, mHeight;
    private Rect mPreviewRect, mDetectRect;

    private int mFaceType = 0;
    private boolean mFaceEnable = false;

    private boolean mEnableSound;
    private AudioManager mAudioManager;

    //    当前帧yuv格式
    byte[] currentFrame;

    String CARD_TEMP="CARD_TEMP.jpg";
    int s=0;




    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CameraView);
        mFaceType = array.getInt(R.styleable.CameraView_face_type, 0);
        array.recycle();

        init();
    }

    private void init() {
        configCamera();
        setSurfaceTextureListener(this);
    }



    private void configCamera() {
        // 获取前置摄像头
        int size = Camera.getNumberOfCameras();
        mCameraInfo = new Camera.CameraInfo();
        for (int id = 0; id < size; id++) {
            Camera.getCameraInfo(id, mCameraInfo);
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                break;
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mWidth = width;
        mHeight = height;
        initCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mWidth = width;
        mHeight = height;
        initCamera();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        currentFrame = data;
        if (!mFaceEnable)
            return;


    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        if (isAvailable())
            initCamera();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        releaseCamera();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {

    }

    /**
     * 获取可支持的最高清的预览分辨率
     *
     * @param parameters 相机属性
     * @return 当无法获取时，返回空
     */
    /**
     * 获取可支持的最高清的预览分辨率
     *
     * @param parameters 相机属性
     * @return 当无法获取时，返回空
     */
    private Camera.Size getBestPreviewSize(@NonNull Camera.Parameters parameters) {
        if (parameters.getSupportedPreviewSizes() == null
                || parameters.getSupportedPreviewSizes().size() == 0)
            return null;

        List<Camera.Size> size_list = parameters.getSupportedPreviewSizes();

//        Camera.Size size = size_list.get(1);
//        for (Camera.Size item : size_list) {
//            if (item.width > size.width)
//                size = item;
//            else if (item.width == size.width && item.height > size.height)
//                size = item;
//        }
        Camera.Size size = size_list.get(0);
        for (Camera.Size item : size_list) {
            if (item.width > size.width)
                size = item;
            else if (item.width == size.width && item.height > size.height)
                size = item;
        }
        return  size;
//        return getOptimalPreviewSize(size_list,mWidth,mHeight);
    }



    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {


        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        Log.e("TAG", "getOptimalPreviewSize: "+optimalSize.width );
        Log.e("TAG", "getOptimalPreviewSize: "+optimalSize.height );
        return optimalSize;
    }
    private void initCamera() {
        releaseCamera();
        Camera.Parameters parameters;
        try {
            mCamera = Camera.open(mCameraId);
            parameters = mCamera.getParameters();
            //获取摄像头支持的各种分辨率
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

        } catch (Exception exception) {
            releaseCamera();
            return;
        }

        Camera.Size mCameraSize = getBestPreviewSize(parameters);

        Log.e("TAG", "initCamera: "+mCameraSize.width );
        Log.e("TAG", "initCamera: "+mCameraSize.height );
        if (mCameraSize == null) {
            releaseCamera();

            return;
        }

        // 获取旋转角度
        Display display = ((WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                mCameraDegree = 0;
                break;
            case Surface.ROTATION_90:
                mCameraDegree = 90;
                break;
            case Surface.ROTATION_180:
                mCameraDegree = 180;
                break;
            case Surface.ROTATION_270:
                mCameraDegree = 270;
                break;
            default:
                mCameraDegree = 0;
                break;
        }
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraDegree = (mCameraInfo.orientation + mCameraDegree) % 360;
            mCameraDegree = (360 - mCameraDegree) % 360;
        } else
            mCameraDegree = (mCameraInfo.orientation - mCameraDegree + 360) % 360;

        // 设置预览尺寸
        parameters.setPreviewSize(mCameraSize.width, mCameraSize.height);
        // 设置对焦模式
        if (parameters.getSupportedFocusModes()
                .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        // 设置旋转角度
        parameters.setRotation(mCameraDegree);
//        parameters.setRotation(0);
        Log.e("TAG", "mCameraDegree: "+mCameraDegree );
        // 设置缩放比例
        float pw;
        float ph;
        if (mCameraDegree % 180 == 0) {
            pw = mCameraSize.width;
            ph = mCameraSize.height;
        } else {
            pw = mCameraSize.height;
            ph = mCameraSize.width;
        }
        float ws = mWidth / pw;
        float hs = mHeight / ph;
        Matrix matrix = new Matrix();
        if (ws > hs)
            matrix.postScale(1F, ws * ph / mHeight,
                    mWidth / 2F, mHeight / 2F);
        else
            matrix.postScale(hs * pw / mWidth, 1F,
                    mWidth / 2F, mHeight / 2F);
        setTransform(matrix);
        // 计算识别区域
        // 计算识别区域
        mPreviewRect = new Rect(0, 0, mCameraSize.width, mCameraSize.height);
        if (mCameraDegree % 180 == 0) {
            float dws = 1F * mCameraSize.width / mWidth;
            float dhs = 1F * mCameraSize.height / mHeight;
            float ds = dws < dhs ? dws : dhs;
            float dw = mWidth * ds;
            float dh = mHeight * ds;
            float dl = (mCameraSize.width - dw) / 2F;
            float dt = (mCameraSize.height - dh) / 2F;
            mDetectRect = new Rect((int) dl, (int) dt, (int) (dl + dw), (int) (dt + dh));
        } else {
            float dws = 1F * mCameraSize.height / mWidth;
            float dhs = 1F * mCameraSize.width / mHeight;
            float ds = dws < dhs ? dws : dhs;
            float dw = mWidth * ds;
            float dh = mHeight * ds;
            float dl = (mCameraSize.height - dw) / 2F;
            float dt = (mCameraSize.width - dh) / 2F;
            mDetectRect = new Rect((int) dl, (int) dt, (int) (dl + dw), (int) (dt + dh));
        }
        try {
            mCamera.setParameters(parameters);

            mCamera.setDisplayOrientation(mCameraDegree);


//          mCamera.setDisplayOrientation(0);

            mCamera.setPreviewCallback(this);
            mCamera.setPreviewTexture(getSurfaceTexture());

            mCamera.startPreview();
        } catch (Exception exception) {
            releaseCamera();
        }
    }
           public static void setCameraDisplayOrientation (Activity activity, int cameraId, Camera camera) {
               Camera.CameraInfo info = new Camera.CameraInfo();
               Camera.getCameraInfo (cameraId , info);
               int rotation = activity.getWindowManager ().getDefaultDisplay ().getRotation ();
               int degrees = 0;
               switch (rotation) {
                   case Surface.ROTATION_0:
                       degrees = 0;
                       break;
                   case Surface.ROTATION_90:
                       degrees = 90;
                       break;
                   case Surface.ROTATION_180:
                       degrees = 180;
                       break;
                   case Surface.ROTATION_270:
                       degrees = 270;
                       break;
               }
               int result;
               if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                   result = (info.orientation + degrees) % 360;
                   result = (360 - result) % 360;   // compensate the mirror
               } else {
                   // back-facing
                   result = ( info.orientation - degrees + 360) % 360;
               }
               camera.setDisplayOrientation (result);
           }

    public void releaseCamera() {
        try {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
        } catch (Exception exception) {
            // Do nothing.
        } finally {
            mCamera = null;
        }

    }

    private String getResString(int res) {
        try {
            return getContext().getString(res);
        } catch (Exception exception) {
            return null;
        }
    }

    public void setFaceEnable(boolean enable) {
            mFaceEnable = enable;
    }


    public File nv21() {

        try {
//            File file = new File(getContext().getExternalCacheDir(), CARD_TEMP);
            File file = new File(getContext().getExternalCacheDir(), System.currentTimeMillis()+".jpg");
            Log.e("TAG", "nv21: "+file.getAbsolutePath() );
//            file.delete();
            YuvImage image = new YuvImage(currentFrame, ImageFormat.NV21,
                    getPreviewWidth(), getPreviewHeight(), null);
            FileOutputStream fos = new FileOutputStream(file);
            image.compressToJpeg(new Rect(0, 0,
                    image.getWidth(), image.getHeight()), 50, fos);
            fos.close();

            return file;
        } catch (Exception exception) {
            // Do nothing.
             return null;
        }

    }

           /**
            * @param imgPaths          图片地址
            * @param pdf_save_address  pdf保存地址
            */
           public  void imageToPDF(String imgPaths, File pdf_save_address) {
               try {
                   Document document = new Document();
                   // 创建PdfWriter对象
                   PdfWriter.getInstance(document, new FileOutputStream(pdf_save_address));
                   document.open();
                   Image img = Image.getInstance(imgPaths);
                   float scale = ((document.getPageSize().getWidth() - document.leftMargin()
                           - document.rightMargin() - 0) / img.getWidth()) * 100;
                   img.scalePercent(scale);
                   img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                   document.add(img);
                   document.close();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
           public String MergePdf2() throws Exception {
               //pdf合并工具类
               PDFMergerUtility mergePdf = new PDFMergerUtility();

               String folder = "D:/testfile";
               String destinationFileName = "mergedTest.pdf";
               String[] filesInFolder = getFiles(folder);

               for(int i = 0; i < filesInFolder.length; i++){
                   //循环添加要合并的pdf存放的路径
                   //File.separatorChar 与系统有关的默认名称分隔符。此字段被初始化为包含系统属性 file.separator 值的第一个字符。在 UNIX 系统上，此字段的值为 '/'；在 Microsoft Windows 系统上，它为 '\'。
                   mergePdf.addSource(folder + File.separatorChar + filesInFolder[i]);
               }
               //设置合并生成pdf文件名称
               mergePdf.setDestinationFileName(folder + File.separator + destinationFileName);
               //合并pdf
               mergePdf.mergeDocuments();
               return "index4";
           }

           public String MergePdf() throws Exception
           {
               //pdf合并工具类
               PDFMergerUtility mergePdf = new PDFMergerUtility();

               String folder = "D:/testfile";
               String destinationFileName = "mergedTest.pdf";
               String[] filesInFolder = getFiles(folder);

               for(int i = 0; i < filesInFolder.length; i++){
                   //循环添加要合并的pdf存放的路径
                   //File.separatorChar 与系统有关的默认名称分隔符。此字段被初始化为包含系统属性 file.separator 值的第一个字符。在 UNIX 系统上，此字段的值为 '/'；在 Microsoft Windows 系统上，它为 '\'。
                   mergePdf.addSource(folder + File.separatorChar + filesInFolder[i]);
               }
               //设置合并生成pdf文件名称
               mergePdf.setDestinationFileName(folder + File.separator + destinationFileName);
               //合并pdf
               mergePdf.mergeDocuments();
               return "index4";
           }
           private String[] getFiles(String folder) throws IOException { //获取文件夹下的全部文件
               File _folder = new File(folder);
               String[] filesInFolder;

               if(_folder.isDirectory()){
                   filesInFolder = _folder.list();
                   return filesInFolder;
               } else {
                   throw new IOException("Path is not a directory");
               }
           }

    public int getPreviewWidth() {
        if (mPreviewRect == null)
            return 0;
        return mPreviewRect.width();
    }

    public int getPreviewHeight() {
        if (mPreviewRect == null)
            return 0;
        return mPreviewRect.height();
    }

}
