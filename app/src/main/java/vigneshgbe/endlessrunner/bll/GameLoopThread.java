package vigneshgbe.endlessrunner.bll;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import vigneshgbe.endlessrunner.gui.GamePanel;

public class GameLoopThread extends Thread {

    public static final int MAX_FPS = 60; // Số khung hình tối đa mỗi giây, được thay đổi từ 30 thành 60
    public static Canvas mCanvas; // Đối tượng Canvas để vẽ

    private SurfaceHolder mSurfaceHolder; // Đối tượng SurfaceHolder để kiểm soát bề mặt vẽ
    private GamePanel mGamePanel; // Đối tượng GamePanel để cập nhật và vẽ game

    private boolean mRunning; // Cờ để kiểm soát vòng lặp game
    private double mAverageFPS; // Biến để theo dõi FPS trung bình

    // Phương thức khởi tạo của GameLoopThread
    public GameLoopThread(SurfaceHolder holder, GamePanel gamePanel) {
        super();
        mSurfaceHolder = holder;
        mGamePanel = gamePanel;
    }

    // Phương thức để thiết lập giá trị của mRunning
    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        long startTime; // Thời gian bắt đầu của mỗi khung hình
        long timeMillis; // Thời gian xử lý của mỗi khung hình
        long waitTime; // Thời gian cần chờ để đạt MAX_FPS
        long targetTime = 1000 / MAX_FPS; // Thời gian mục tiêu mỗi khung hình (ms)

        while (mRunning) {
            startTime = System.nanoTime();
            mCanvas = null;

            // Vòng lặp này duy trì việc chạy game.
            try {
                // Cố gắng lấy bề mặt của SurfaceHolder trong trạng thái có thể chỉnh sửa.
                mCanvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    // Gọi GamePanel để cập nhật và vẽ, tiếp tục game.
                    // Cần phải thực hiện trong khối synchronized.
                    mGamePanel.update();
                    mGamePanel.draw(mCanvas);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    try {
                        // Mở khóa Canvas để các thay đổi được hiển thị.
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // Đảm bảo không vượt quá MAX_FPS.
            // Đầu tiên, tìm thời gian tính bằng mili giây kể từ khi bắt đầu khung hình này.
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            // Xem liệu khung hình có hoàn thành nhanh hơn mong muốn không.
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    // Nếu mất ít thời gian hơn, ngủ trong thời gian còn lại trước khi tạo khung hình tiếp theo.
                    this.sleep(waitTime);
                }
            } catch (InterruptedException iex) {
                iex.printStackTrace();
            }
        }
    }
}
