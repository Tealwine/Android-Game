package vigneshgbe.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import vigneshgbe.endlessrunner.be.Constants;

public class HealthManager {

    // Các hằng số để định nghĩa kích thước và vị trí của thanh máu
    private final int BAR_WIDTH = 400;
    private final int BAR_HEIGHT = 100;
    private final int HEALTH_MULTIPLIER = 8; // Hệ số để chuyển đổi sát thương thành kích thước thanh máu
    private final int LEFT_COORDINATE = Constants.SCREEN_WIDTH - (BAR_WIDTH * 2);
    private final int RIGHT_COORDINATE = LEFT_COORDINATE + BAR_WIDTH;
    private final int TOP_COORDINATE = (int)(Constants.START_FROM_TOP * 1.5f);
    private final int BOTTOM_COORDINATE = (int)(Constants.START_FROM_TOP * 1.5f) + BAR_HEIGHT;

    // Các đối tượng Rect để vẽ thanh máu và viền
    private Rect mRectDamage;
    private Rect mRectHealth;
    private Rect mRectBorder;

    public HealthManager(){
        // Khởi tạo các đối tượng Rect cho thanh máu, thanh sát thương và viền
        mRectDamage = new Rect(LEFT_COORDINATE, TOP_COORDINATE, LEFT_COORDINATE, BAR_HEIGHT);
        mRectHealth = new Rect(LEFT_COORDINATE, TOP_COORDINATE, RIGHT_COORDINATE, BOTTOM_COORDINATE);
        mRectBorder = new Rect(LEFT_COORDINATE - 5, TOP_COORDINATE - 5, RIGHT_COORDINATE + 5, BOTTOM_COORDINATE + 5);
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();

        // Vẽ viền của thanh máu
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectBorder, paint);

        // Vẽ thanh sát thương
        paint.setColor(Color.RED);
        canvas.drawRect(mRectDamage, paint);

        // Vẽ thanh máu
        paint.setColor(Color.GREEN);
        canvas.drawRect(mRectHealth, paint);
    }

    // Cập nhật thanh máu dựa trên sát thương đã nhận
    public boolean update(int damageTaken){
        if(damageTaken <= BAR_WIDTH / HEALTH_MULTIPLIER){
            damageTaken = damageTaken * HEALTH_MULTIPLIER;
            mRectDamage.set(LEFT_COORDINATE, TOP_COORDINATE, LEFT_COORDINATE + damageTaken, BOTTOM_COORDINATE);
            mRectHealth.set(LEFT_COORDINATE + damageTaken, TOP_COORDINATE, RIGHT_COORDINATE, BOTTOM_COORDINATE);
            return false;
        }
        return true;
    }
}
