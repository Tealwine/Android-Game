package vigneshgbe.endlessrunner.bll.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import vigneshgbe.endlessrunner.R;
import vigneshgbe.endlessrunner.be.Cloud;
import vigneshgbe.endlessrunner.be.Constants;
import vigneshgbe.endlessrunner.be.IGameObject;

public class CloudManager implements IGameObject {

    // Các hằng số để định nghĩa kích thước và tốc độ của đám mây
    private final int CLOUD_MINIMUM_HEIGHT = 200;
    private final int CLOUD_MAXIMUM_HEIGHT = 400;
    private final int CLOUD_MINIMUM_WIDTH = 200;
    private final int CLOUD_MAXIMUM_WIDTH = 400;

    private final int CLOUD_MAXIMUM_Y_COORDINATE = Constants.SCREEN_HEIGHT - (CLOUD_MAXIMUM_HEIGHT * 2);

    private final int CLOUD_SPEED = 5;

    private List<Cloud> mClouds; // Danh sách các đám mây
    private List<Bitmap> mImages; // Danh sách hình ảnh của các đám mây

    public CloudManager(){
        createImages(); // Tạo các hình ảnh đám mây
        populateClouds(); // Tạo các đám mây lúc khởi đầu
    }

    /**
     * Tạo tất cả hình ảnh được sử dụng cho các đám mây.
     */
    private void createImages() {
        mImages = new ArrayList<>();
        BitmapFactory bf = new BitmapFactory();
        mImages.add(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud1));
        mImages.add(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud2));
        mImages.add(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud3));
        mImages.add(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud4));
        mImages.add(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud5));
    }

    /**
     * Tạo tất cả các đám mây lúc khởi đầu của trò chơi.
     */
    private void populateClouds() {
        mClouds = new ArrayList<>();
        for(Bitmap image : mImages){
            mClouds.add(createCloud(image)); // Tạo và thêm các đám mây vào danh sách
        }
    }

    /**
     * Tạo một đám mây với Bitmap đã cho, có kích thước và vị trí ngẫu nhiên.
     * @param image Hình ảnh của đám mây
     * @return Đối tượng Cloud
     */
    private Cloud createCloud(Bitmap image){
        double height = Math.random() * CLOUD_MAXIMUM_HEIGHT + CLOUD_MINIMUM_HEIGHT;
        double width = Math.random() * CLOUD_MAXIMUM_WIDTH + CLOUD_MINIMUM_WIDTH;

        double yCoordinate = Math.random() * CLOUD_MAXIMUM_Y_COORDINATE;
        double xCoordinate = (Math.random() * Constants.SCREEN_WIDTH) + Constants.SCREEN_WIDTH;

        Rect rect = new Rect((int)xCoordinate, (int)yCoordinate, (int)(xCoordinate + width), (int)(yCoordinate + height));

        return new Cloud(rect, image, CLOUD_SPEED); // Trả về đối tượng Cloud
    }

    @Override
    public void draw(Canvas canvas) {
        for(Cloud cloud : mClouds){
            cloud.draw(canvas); // Vẽ các đám mây lên canvas
        }
    }

    @Override
    public void update() {
        for(Cloud cloud : mClouds){
            cloud.move(); // Di chuyển các đám mây
        }

        // Kiểm tra nếu một đám mây đã vượt qua màn hình. Nếu có - xóa nó và tạo một đám mây mới với cùng hình ảnh.
        if(mClouds.get(mClouds.size() - 1).getRect().right <= 0){
            int pos = mClouds.size() - 1;
            Bitmap image = mClouds.get(pos).getImage();
            mClouds.remove(pos); // Xóa đám mây cuối cùng
            mClouds.add(0, createCloud(image)); // Tạo và thêm đám mây mới vào đầu danh sách
        }
    }
}
