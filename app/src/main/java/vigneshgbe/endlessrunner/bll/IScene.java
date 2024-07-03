package vigneshgbe.endlessrunner.bll;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface IScene {
    void update();
    void draw(Canvas canvas);
    void terminate();
    void recieveTouch(MotionEvent event);
    void setHighScore(int highScore); // Thêm phương thức này để thiết lập điểm cao nhất
}
