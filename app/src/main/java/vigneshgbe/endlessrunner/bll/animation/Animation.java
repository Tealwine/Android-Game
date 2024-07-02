package vigneshgbe.endlessrunner.bll.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {

    private Bitmap[] mFrames; // Mảng chứa các frame của animation
    private int mFrameIndex; // Chỉ số của frame hiện tại
    private float mFrameTime; // Thời gian mỗi frame được hiển thị
    private long mLastFrame; // Thời điểm frame cuối cùng được cập nhật

    private boolean mIsPlaying; // Trạng thái đang chơi animation

    public Animation(Bitmap[] frames, float animationTime){
        mIsPlaying = false; // Khởi tạo trạng thái không chơi animation
        mFrames = frames; // Gán các frame của animation
        mFrameIndex = 0; // Khởi tạo chỉ số frame ban đầu
        mFrameTime = animationTime / mFrames.length; // Tính thời gian hiển thị mỗi frame
        mLastFrame = System.currentTimeMillis(); // Lấy thời gian hiện tại
    }

    public boolean isPlaying(){
        return mIsPlaying; // Trả về trạng thái đang chơi animation
    }

    public void play(){
        mIsPlaying = true; // Bắt đầu chơi animation
        mFrameIndex = 0; // Đặt chỉ số frame về 0
        mLastFrame = System.currentTimeMillis(); // Lấy thời gian hiện tại
    }

    public void stop(){
        mIsPlaying = false; // Dừng chơi animation
    }

    public void update(){
        if(!isPlaying()){
            return; // Nếu không chơi animation thì thoát
        }
        if(System.currentTimeMillis() - mLastFrame > mFrameTime * 1000){
            mFrameIndex++; // Tăng chỉ số frame
            mFrameIndex = mFrameIndex >= mFrames.length ? 0 : mFrameIndex; // Quay lại frame đầu nếu đã đến frame cuối
            mLastFrame = System.currentTimeMillis(); // Cập nhật thời gian của frame cuối cùng
        }
    }

    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying()){
            return; // Nếu không chơi animation thì thoát
        }
        scaleRect(destination); // Tỉ lệ lại Rect đích
        canvas.drawBitmap(mFrames[mFrameIndex], null, destination, new Paint()); // Vẽ frame hiện tại lên canvas
    }

    private void scaleRect(Rect rect) {
        float whRatio = (float)(mFrames[mFrameIndex].getWidth()) / (float)(mFrames[mFrameIndex].getHeight());
        if(rect.width() > rect.height()){
            rect.left = rect.height() - (int)((rect.height() * whRatio)); // Điều chỉnh tỉ lệ theo chiều rộng
        }else{
            rect.top = rect.bottom - (int)((rect.width() * (1/whRatio))); // Điều chỉnh tỉ lệ theo chiều cao
        }
    }
}
