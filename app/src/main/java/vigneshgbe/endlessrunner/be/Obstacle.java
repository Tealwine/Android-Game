package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import vigneshgbe.endlessrunner.R;

public class Obstacle implements IGameObject {

    private Rect mRect; // Đối tượng Rect để định vị vị trí của chướng ngại vật
    private int mColor; // Màu sắc của chướng ngại vật (không được sử dụng trong ví dụ này)

    private Bitmap mImage; // Hình ảnh của chướng ngại vật

    public Obstacle(int rectHeight, int obstacleWidth, int startX, int startY, int color){
        // Khởi tạo đối tượng Rect với các thông số vị trí và kích thước
        mRect = new Rect(startX, startY, startX + obstacleWidth, startY + rectHeight);
        mColor = color; // Gán màu sắc (không được sử dụng trong ví dụ này)

        // Tạo đối tượng Bitmap từ tài nguyên drawable singlegate
        BitmapFactory bf = new BitmapFactory();
        mImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.singlegate);
    }

    @Override
    public void draw(Canvas canvas) {
        // Vẽ hình ảnh của chướng ngại vật lên canvas tại vị trí đã xác định
        canvas.drawBitmap(mImage, null, mRect, new Paint());
    }

    @Override
    public void update() {
        // Phương thức cập nhật trạng thái của chướng ngại vật (chưa được triển khai)
    }

    public void move(float speed){
        // Di chuyển chướng ngại vật sang trái dựa trên tốc độ được cung cấp
        mRect.left -= speed;
        mRect.right -= speed;
    }

    public Rect getRect(){
        // Phương thức trả về đối tượng Rect định vị vị trí của chướng ngại vật
        return mRect;
    }

    public boolean collisionWithPlayer(Rect playerRect){
        // Phương thức kiểm tra va chạm với người chơi dựa trên việc xem liệu vùng bao quanh của người chơi và chướng ngại vật có giao nhau không
        return mRect.intersects(playerRect.left, playerRect.top, playerRect.right, playerRect.bottom);
    }
}
