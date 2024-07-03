package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import vigneshgbe.endlessrunner.bll.IScene;

public class Cloud implements IGameObject {

    private Rect mRect; // Đối tượng Rect để định vị vị trí của đám mây trên màn hình
    private Bitmap mImage; // Hình ảnh của đám mây
    private int mSpeed; // Tốc độ di chuyển của đám mây

    public Cloud(Rect rect, Bitmap image, int speed) {
        mRect = rect;
        mImage = image;
        mSpeed = speed;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mImage, null, mRect, new Paint());
    }

    @Override
    public void update() {
        // Tạm thời không cần triển khai phương thức update vì đối tượng Cloud không cần cập nhật logic phức tạp
    }

    public void move() {
        mRect.right -= mSpeed;
        mRect.left -= mSpeed;
    }

    public void reset(float left, float top, float right, float bottom, Bitmap image, int speed) {
        mRect.set((int) left, (int) top, (int) right, (int) bottom);
        mImage = image;
        mSpeed = speed;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public Rect getRect() {
        return mRect;
    }
}
