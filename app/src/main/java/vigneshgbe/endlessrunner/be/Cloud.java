package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Cloud implements IGameObject { // Định nghĩa lớp Cloud và triển khai giao diện IGameObject

    private Rect mRect; // Đối tượng Rect để định vị vị trí của đám mây trên màn hình
    private Bitmap mImage; // Hình ảnh của đám mây
    private int mSpeed; // Tốc độ di chuyển của đám mây

    public Cloud(Rect rect, Bitmap image, int speed){ // Constructor của lớp Cloud
        mRect = rect;
        mImage = image;
        mSpeed = speed;
    }

    @Override
    public void draw(Canvas canvas) { // Phương thức vẽ đám mây lên canvas
        canvas.drawBitmap(mImage, null, mRect, new Paint());
    }

    @Override
    public void update() { // Phương thức cập nhật trạng thái của đám mây (chưa được triển khai)

    }

    public void move(){ // Phương thức di chuyển đám mây sang trái dựa vào tốc độ
        mRect.right -= mSpeed;
        mRect.left -= mSpeed;
    }

    public Bitmap getImage(){ // Phương thức trả về hình ảnh của đám mây
        return mImage;
    }

    public Rect getRect(){ // Phương thức trả về đối tượng Rect định vị vị trí của đám mây
        return mRect;
    }
}