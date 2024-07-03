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

    private final int CLOUD_SPEED = 4;

    private List<Cloud> mClouds; // Danh sách các đám mây
    private Bitmap[] mCloudImages; // Mảng hình ảnh của các đám mây

    public CloudManager() {
        createImages(); // Tạo các hình ảnh đám mây
        populateClouds(); // Tạo các đám mây lúc khởi đầu
    }

    /**
     * Tạo tất cả hình ảnh được sử dụng cho các đám mây.
     */
    private void createImages() {
        mCloudImages = new Bitmap[5]; // Số lượng hình ảnh đám mây
        BitmapFactory bf = new BitmapFactory();
        for (int i = 0; i < mCloudImages.length; i++) {
            Bitmap originalBitmap = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.cloud1 + i);
            mCloudImages[i] = scaleBitmap(originalBitmap);
        }
    }

    /**
     * Scale Bitmap về kích thước hợp lý.
     */
    private Bitmap scaleBitmap(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        // Giới hạn kích thước tối đa và tối thiểu
        int maxWidth = originalWidth * 3;
        int maxHeight = originalHeight * 3;
        int minWidth = originalWidth;
        int minHeight = originalHeight;

        // Tính toán tỷ lệ scale
        float widthRatio = (float) maxWidth / originalWidth;
        float heightRatio = (float) maxHeight / originalHeight;
        float scaleRatio = Math.min(widthRatio, heightRatio);

        // Scale Bitmap
        if (scaleRatio < 1.0) {
            int newWidth = Math.round(originalWidth * scaleRatio);
            int newHeight = Math.round(originalHeight * scaleRatio);
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }

        // Đảm bảo kích thước không nhỏ hơn kích thước ban đầu
        if (bitmap.getWidth() < minWidth || bitmap.getHeight() < minHeight) {
            bitmap = Bitmap.createScaledBitmap(bitmap, minWidth, minHeight, true);
        }

        return bitmap;
    }

    /**
     * Tạo tất cả các đám mây lúc khởi đầu của trò chơi.
     */
    private void populateClouds() {
        mClouds = new ArrayList<>();
        for (Bitmap image : mCloudImages) {
            mClouds.add(createCloud(image)); // Tạo và thêm các đám mây vào danh sách
        }
    }

    /**
     * Tạo một đám mây với Bitmap đã cho, có kích thước và vị trí ngẫu nhiên.
     *
     * @param image Hình ảnh của đám mây
     * @return Đối tượng Cloud
     */
    private Cloud createCloud(Bitmap image) {
        double height = Math.random() * CLOUD_MAXIMUM_HEIGHT + CLOUD_MINIMUM_HEIGHT;
        double width = Math.random() * CLOUD_MAXIMUM_WIDTH + CLOUD_MINIMUM_WIDTH;

        double yCoordinate = Math.random() * CLOUD_MAXIMUM_Y_COORDINATE;
        double xCoordinate = (Math.random() * Constants.SCREEN_WIDTH) + Constants.SCREEN_WIDTH;

        Rect rect = new Rect((int) xCoordinate, (int) yCoordinate, (int) (xCoordinate + width), (int) (yCoordinate + height));

        return new Cloud(rect, image, CLOUD_SPEED); // Trả về đối tượng Cloud
    }

    @Override
    public void draw(Canvas canvas) {
        for (Cloud cloud : mClouds) {
            cloud.draw(canvas); // Vẽ các đám mây lên canvas
        }
    }

    @Override
    public void update() {
        for (Cloud cloud : mClouds) {
            cloud.move(); // Move the clouds
            if (cloud.getRect().right <= 0) {
                // If the cloud moves off the left side of the screen, reset its position and reuse it
                Bitmap image = cloud.getImage(); // Get the current cloud's image
                cloud.reset(
                        Constants.SCREEN_WIDTH + (float) (Math.random() * Constants.SCREEN_WIDTH), // New X coordinate
                        (float) (Math.random() * CLOUD_MAXIMUM_Y_COORDINATE), // New Y coordinate
                        Constants.SCREEN_WIDTH + (float) (Math.random() * Constants.SCREEN_WIDTH) + image.getWidth(), // New right coordinate
                        (float) ((Math.random() * CLOUD_MAXIMUM_Y_COORDINATE) + image.getHeight()), // New bottom coordinate
                        image, // Use the same image for reset
                        CLOUD_SPEED // Use the same speed for reset
                );
            }
        }
    }
}
