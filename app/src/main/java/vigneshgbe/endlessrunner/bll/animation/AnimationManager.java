package vigneshgbe.endlessrunner.bll.animation;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationManager {

    private Animation[] mAnimations; // Mảng chứa các animation
    private int mAnimationIndex; // Chỉ số của animation hiện tại

    public AnimationManager(Animation[] animations){
        mAnimations = animations; // Gán các animation
        mAnimationIndex = 0; // Khởi tạo chỉ số animation ban đầu
    }

    public void playAnimation(int index){
        // Lặp qua tất cả các animation
        for(int i = 0; i < mAnimations.length; i++){
            if(i == index){
                // Nếu là animation được chọn và chưa được chơi, bắt đầu chơi animation
                if(!mAnimations[i].isPlaying()){
                    mAnimations[i].play();
                }
            } else {
                // Nếu không phải animation được chọn, dừng chơi animation
                mAnimations[i].stop();
            }
        }
        mAnimationIndex = index; // Cập nhật chỉ số animation hiện tại
    }

    public void draw(Canvas canvas, Rect rect){
        // Vẽ animation hiện tại lên canvas nếu nó đang chơi
        if(mAnimations[mAnimationIndex].isPlaying()){
            mAnimations[mAnimationIndex].draw(canvas, rect);
        }
    }

    public void update(){
        // Cập nhật animation hiện tại nếu nó đang chơi
        if(mAnimations[mAnimationIndex].isPlaying()){
            mAnimations[mAnimationIndex].update();
        }
    }
}
