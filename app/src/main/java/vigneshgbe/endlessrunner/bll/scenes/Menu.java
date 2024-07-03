package vigneshgbe.endlessrunner.bll.scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import vigneshgbe.endlessrunner.be.Constants;
import vigneshgbe.endlessrunner.be.Floor;
import vigneshgbe.endlessrunner.bll.IScene;
import vigneshgbe.endlessrunner.bll.managers.SceneManager;

public class Menu implements IScene {

    private Floor mFloor;
    private Rect mTextRect;
    private int mHighScore;
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE";

    public Menu() {
        mFloor = new Floor(new Rect());
        mTextRect = new Rect();

        // Đọc điểm cao nhất từ SharedPreferences
        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        mHighScore = prefs.getInt(HIGH_SCORE_KEY, 0);
    }

    @Override
    public void update() {
        // Không cần cập nhật gì cho menu
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#e5faff"));
        mFloor.draw(canvas);
        drawText(canvas, "Endless Runner", "Tap anywhere to begin");
        drawHighScore(canvas);
    }

    @Override
    public void terminate() {
        // Không cần gì để kết thúc menu
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                SceneManager.ACTIVE_SCENE = Constants.GAME_SCENE;
                break;
            }
        }
    }

    @Override
    public void setHighScore(int highScore) {
        mHighScore = highScore;

        // Lưu điểm cao nhất vào SharedPreferences
        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }

    private void drawText(Canvas canvas, String headLine, String text) {
        Paint paint = new Paint();
        paint.setTextSize(200);
        paint.setColor(Color.BLUE);
        paint.setShadowLayer(5, 0, 0, Color.BLACK);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.getClipBounds(mTextRect);
        int cHeight = mTextRect.height();
        int cWidth = mTextRect.width();
        paint.getTextBounds(headLine, 0, headLine.length(), mTextRect);
        float x = cWidth / 2f;
        float y = cHeight / 4f;
        canvas.drawText(headLine, x, y, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        y = cHeight / 2f;
        canvas.drawText(text, x, y, paint);
    }

    private void drawHighScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.getClipBounds(mTextRect);
        int cHeight = mTextRect.height();
        int cWidth = mTextRect.width();
        float x = cWidth / 2f;
        float y = (cHeight / 4f) * 3; // Vị trí để hiển thị điểm cao nhất
        canvas.drawText("High Score: " + mHighScore, x, y, paint);
    }
}
