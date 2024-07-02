package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import vigneshgbe.endlessrunner.R;
import vigneshgbe.endlessrunner.bll.animation.Animation;
import vigneshgbe.endlessrunner.bll.animation.AnimationManager;

public class Player implements IGameObject {

    private Rect mRect; // Đối tượng Rect để định vị vị trí của người chơi
    private int mColor; // Màu sắc của người chơi (không được sử dụng trong ví dụ này)

    private Animation mRunAnimation; // Animation cho trạng thái chạy của người chơi
    private Animation mJumpAnimation; // Animation cho trạng thái nhảy của người chơi

    private AnimationManager mAnimationManager; // Quản lý các animation của người chơi

    public Player(Rect rect, int color){
        mRect = rect; // Khởi tạo đối tượng Rect với vị trí và kích thước được cung cấp
        mColor = color; // Gán màu sắc (không được sử dụng trong ví dụ này)

        createStickAnimations(); // Tạo các animation cho người chơi

        mAnimationManager = new AnimationManager(new Animation[]{mRunAnimation, mJumpAnimation});
        // Khởi tạo AnimationManager để quản lý các animation của người chơi
    }

    @Override
    public void draw(Canvas canvas) {
        mAnimationManager.draw(canvas, mRect); // Vẽ người chơi lên canvas dựa trên trạng thái animation hiện tại
    }

    @Override
    public void update() {
        mAnimationManager.update(); // Cập nhật trạng thái animation của người chơi
    }

    public void update(Point point, boolean isJumping){
        // Cập nhật vị trí của người chơi dựa trên vị trí mới và trạng thái nhảy
        mRect.set(point.x - mRect.width()/2,
                point.y - mRect.height()/2,
                point.x + mRect.width()/2,
                point.y + mRect.height()/2);

        if(isJumping){
            mAnimationManager.playAnimation(1); // Chơi animation nhảy
            mAnimationManager.update();
        } else {
            mAnimationManager.playAnimation(0); // Chơi animation chạy
            mAnimationManager.update();
        }
    }

    public Rect getRect(){
        return mRect; // Trả về đối tượng Rect định vị vị trí của người chơi
    }

    private void createStickAnimations(){
        BitmapFactory bf = new BitmapFactory();
        // Tạo các Bitmap từ tài nguyên drawable layer1 đến layer12
        Bitmap img1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer1);
        Bitmap img2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer2);
        Bitmap img3 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer3);
        Bitmap img4 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer4);
        Bitmap img5 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer5);
        Bitmap img6 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer6);
        Bitmap img7 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer7);
        Bitmap img8 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer8);
        Bitmap img9 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer9);
        Bitmap img10 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer10);
        Bitmap img11 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer11);
        Bitmap img12 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.layer12);

        // Tạo animation chạy với các frame từ img1 đến img12 và thời gian chuyển frame là 0.5 giây
        mRunAnimation = new Animation(new Bitmap[]{img1, img2, img3, img4, img5, img6,
                img7, img8, img9, img10, img11, img12},
                0.5f);

        // Tạo animation nhảy với một frame duy nhất là img12 và thời gian chuyển frame là 2 giây
        mJumpAnimation = new Animation(new Bitmap[]{img12}, 2);
    }

}