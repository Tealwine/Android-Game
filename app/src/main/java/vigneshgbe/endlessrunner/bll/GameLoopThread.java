package vigneshgbe.endlessrunner.bll;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import vigneshgbe.endlessrunner.gui.GamePanel;

public class GameLoopThread extends Thread {
    public static final int MAX_FPS = 60;
    private Canvas mCanvas;

    private SurfaceHolder mSurfaceHolder;
    private GamePanel mGamePanel;

    private boolean mRunning;
    private double mAverageFPS;

    public GameLoopThread(SurfaceHolder holder, GamePanel gamePanel) {
        super();
        mSurfaceHolder = holder;
        mGamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long targetTime = 1000 / MAX_FPS;
        long totalTime = 0;
        int frameCount = 0;

        while (mRunning) {
            startTime = System.nanoTime();
            mCanvas = null;

            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    if (mCanvas != null) {
                        mGamePanel.update();
                        mGamePanel.draw(mCanvas);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    try {
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (InterruptedException iex) {
                iex.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                mAverageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }
}

