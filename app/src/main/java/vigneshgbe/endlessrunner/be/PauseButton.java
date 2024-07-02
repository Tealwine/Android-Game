package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import vigneshgbe.endlessrunner.R;

public class PauseButton {

    private final int WIDTH = 175; // Độ rộng của nút pause/play
    private final int HEIGHT = 175; // Độ cao của nút pause/play
    private final int X_COORDINATE = Constants.SCREEN_WIDTH - (int)(WIDTH * 1.5f); // Tọa độ X của nút
    private final int Y_COORDINATE = Constants.START_FROM_TOP; // Tọa độ Y của nút

    private Rect mRect; // Đối tượng Rect để định vị vị trí của nút

    private Bitmap mPauseImage; // Hình ảnh của nút pause
    private Bitmap mPlayImage; // Hình ảnh của nút play

    public PauseButton(){
        // Khởi tạo đối tượng Rect với các thông số vị trí và kích thước
        mRect = new Rect(X_COORDINATE, Y_COORDINATE, X_COORDINATE + WIDTH, Y_COORDINATE + HEIGHT);

        // Tạo đối tượng Bitmap từ tài nguyên drawable pause và play
        BitmapFactory bf = new BitmapFactory();
        mPauseImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause);
        mPlayImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.play);
    }

    public Rect getRect(){
        // Phương thức trả về đối tượng Rect định vị vị trí của nút
        return mRect;
    }

    public void draw(Canvas canvas, boolean isPaused) {
        // Vẽ hình ảnh của nút pause hoặc play lên canvas tại vị trí đã xác định
        if(isPaused){
            canvas.drawBitmap(mPlayImage, null, mRect, new Paint());
        } else {
            canvas.drawBitmap(mPauseImage, null, mRect, new Paint());
        }
    }
}
