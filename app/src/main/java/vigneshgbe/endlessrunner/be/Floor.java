package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import vigneshgbe.endlessrunner.R;

public class Floor implements IGameObject {

    private final int WIDTH = 150; // Độ rộng của mỗi mảnh sàn
    private final int Y_COORDINATE = Constants.SCREEN_HEIGHT - 50; // Tọa độ Y của sàn

    private Rect mRect; // Đối tượng Rect để định vị vị trí của sàn

    private List<Rect> mRectList; // Danh sách các đối tượng Rect để lưu trữ các mảnh sàn
    private Bitmap mFloorImage; // Hình ảnh của sàn

    public Floor(Rect rect){
        mRect = rect;
        mRect.set(0, Y_COORDINATE, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT); // Đặt vị trí và kích thước ban đầu cho sàn

        // Tạo đối tượng Bitmap từ tài nguyên drawable floor
        BitmapFactory bf = new BitmapFactory();
        mFloorImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.floor);

        // Khởi tạo danh sách các mảnh sàn
        mRectList = new ArrayList<>();
        for(int i = 0; i < Constants.SCREEN_WIDTH + (Constants.SCREEN_WIDTH/ 2); i += WIDTH){
            mRectList.add(new Rect(i, Y_COORDINATE, i + WIDTH, Constants.SCREEN_HEIGHT));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // Vẽ từng mảnh sàn lên canvas
        for(Rect rect : mRectList){
            canvas.drawBitmap(mFloorImage, null, rect, new Paint());
        }
    }

    @Override
    public void update() {
        // Cập nhật vị trí của từng mảnh sàn dựa trên tốc độ di chuyển
        for(Rect rect : mRectList){
            rect.set(rect.left - (int)Constants.SPEED, Y_COORDINATE, rect.right - (int)Constants.SPEED, Constants.SCREEN_HEIGHT);
        }

        // Kiểm tra nếu một mảnh sàn đã đi qua màn hình, đặt lại nó vào vòng lặp lại
        if(mRectList.get(0).right <= 0){
            Rect rect = mRectList.remove(0);
            int x = mRectList.get(mRectList.size() - 1).right;
            rect.set(x, Y_COORDINATE, x + WIDTH, Constants.SCREEN_HEIGHT);
            mRectList.add(rect);
        }
    }

    public Rect getRect(){
        return mRect;
    }
}