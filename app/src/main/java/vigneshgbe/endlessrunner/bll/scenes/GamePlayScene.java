package vigneshgbe.endlessrunner.bll.scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import vigneshgbe.endlessrunner.R;
import vigneshgbe.endlessrunner.be.Constants;
import vigneshgbe.endlessrunner.be.Floor;
import vigneshgbe.endlessrunner.be.PauseButton;
import vigneshgbe.endlessrunner.be.Player;
import vigneshgbe.endlessrunner.bll.IScene;
import vigneshgbe.endlessrunner.bll.managers.CloudManager;
import vigneshgbe.endlessrunner.bll.managers.GravityManager;
import vigneshgbe.endlessrunner.bll.managers.HealthManager;
import vigneshgbe.endlessrunner.bll.managers.ObstacleManager;
import vigneshgbe.endlessrunner.bll.managers.SceneManager;
import android.content.Context;
import android.content.SharedPreferences;
public class GamePlayScene implements IScene {

    private final int X_POSITION = Constants.SCREEN_WIDTH / 4;
    private final int GRAVITY_THRESHOLD = 20;
    private final int UPDATE_TIMER_INTERVAL = 2000;

    private GravityManager mGravityManager;
    private ObstacleManager mObstacleManager;
    private HealthManager mHealthManager;
    private CloudManager mCloudManager;

    private Rect mTextRect;
    private Paint mPaint;
    private PauseButton mPauseButton;

    private Player mPlayer;
    private Point mPlayerPoint;
    private int mHighScore;
    private Floor mFloor;

    private float mGravity;
    private boolean mIsJumping;
    private boolean mDoubleJumpAvailable;
    private boolean mAllowedToJump;

    private boolean mGameOver;
    private boolean mIsPaused;
    private final String HIGH_SCORE_KEY = "HIGH_SCORE";
    private int mAmountOfDamage;
    private int mScore;

    private boolean mIsTimerStarted;
    private TimerTask mScoreTimerTask;
    private Timer mScoreTimer;

    private MediaPlayer mMediaPlayer;
    private MediaPlayer mJumpSoundPlayer;

    private Bitmap mBackground;

    public GamePlayScene(){
        newGame();
        mTextRect = new Rect();
        SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        mHighScore = prefs.getInt(HIGH_SCORE_KEY, 0);
    }

    private void initMusic() {
        mMediaPlayer = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.game_music);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    private void initJumpSound() {
        mJumpSoundPlayer = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.jump_sound);
    }

    private void initBackground() {
        mBackground = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        mBackground = Bitmap.createScaledBitmap(mBackground, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true);
        mPaint = new Paint();
        mPaint.setColor(Color.argb(150, 0, 0, 0)); // Màu đen với alpha là 150 (tối đa là 255)

    }

    @Override
    public void update() {
        if(!mGameOver && !mIsPaused){
            mPlayer.update(mPlayerPoint, mIsJumping);
            mFloor.update();
            mCloudManager.update();

            playerGravity();
            checkCollisionObstacle();

            mObstacleManager.update();
            if(mHealthManager.update(mAmountOfDamage)){
                mGameOver = true;
                stopMusic();
                checkAndUpdateHighScore(); // Kiểm tra và cập nhật điểm cao nhất nếu cần
            }

            if(!mIsTimerStarted){
                mScoreTimer.scheduleAtFixedRate(mScoreTimerTask, UPDATE_TIMER_INTERVAL, UPDATE_TIMER_INTERVAL);
                mIsTimerStarted = true;
            }
        }
    }
    private void checkAndUpdateHighScore() {
        if (mScore > mHighScore) {
            mHighScore = mScore;
            SharedPreferences prefs = Constants.CURRENT_CONTEXT.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(HIGH_SCORE_KEY, mHighScore);
            editor.apply();
        }
    }

    private void stopMusic() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void stopJumpSound() {
        if (mJumpSoundPlayer != null) {
            if (mJumpSoundPlayer.isPlaying()) {
                mJumpSoundPlayer.stop();
            }
            mJumpSoundPlayer.release();
            mJumpSoundPlayer = null;
        }
    }

    private void pauseMusic() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    private void resumeMusic() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    /**
     * If the player is colliding with an obstacle, increase amountOfDamage.
     */
    private void checkCollisionObstacle() {
        if(mObstacleManager.collisionWithPlayer(mPlayer.getRect())){
            mAmountOfDamage++;
        }
    }

    /**
     * Checks if the player is suspended in the air. If yes - adjust the gravity accordingly.
     */
    private void playerGravity() {
        mPlayerPoint.set(mPlayerPoint.x, mPlayerPoint.y + (int) mGravity);
        if(!mAllowedToJump && !mGravityManager.isPlayerNotTouchingFloor(mPlayer, mFloor)){
            mGravity = 0;
            mIsJumping = false;
            mDoubleJumpAvailable = true;
            mPlayerPoint.set(mPlayerPoint.x, Constants.SCREEN_HEIGHT - 120);
        }else if(mIsJumping && mGravity < 0){
            mGravity += 1;
        }else if(mIsJumping && mGravity < GRAVITY_THRESHOLD){
            mGravity += 1.5f;
        }

    }

    /**
     * Adjust the gravity so the player "jumps".
     */
    private void jump(){
        if(!mIsJumping) {
            mGravity = -GRAVITY_THRESHOLD;
            mIsJumping = true;
            playJumpSound();
        }else if(mIsJumping && mDoubleJumpAvailable){
            mGravity = -GRAVITY_THRESHOLD;
            mDoubleJumpAvailable = false;
            playJumpSound();
        }
    }

    private void playJumpSound() {
        if (mJumpSoundPlayer != null) {
            mJumpSoundPlayer.start();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // Vẽ hình nền
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, mPaint);
        mCloudManager.draw(canvas);

        mFloor.draw(canvas);
        mObstacleManager.draw(canvas);
        mPlayer.draw(canvas);

        mPauseButton.draw(canvas, mIsPaused);

        Paint paint = new Paint();
        drawScore(canvas, paint);

        mHealthManager.draw(canvas);

        if(mGameOver){
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(5, 0, 0, Color.BLACK);
            drawCenterText(canvas, paint, "Game over!", "Score: " + mScore, "High Score: " + mHighScore);
        }
    }

    /**
     * Draws the score on the screen.
     * @param canvas
     * @param paint
     */
    private void drawScore(Canvas canvas, Paint paint) {
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        canvas.drawText("Score: " + mScore, 50, 50 + paint.descent() - paint.ascent(), paint);
        canvas.drawText("High Score: " + mHighScore, 50, 150 + paint.descent() - paint.ascent(), paint);
    }


    @Override
    public void terminate() {
        stopMusic();
        stopJumpSound();
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if(mPauseButton.getRect().contains((int)event.getX(), (int)event.getY())){
                    mIsPaused = !mIsPaused;
                    if (mIsPaused) {
                        pauseMusic();
                    } else {
                        resumeMusic();
                    }
                }else if (!mGameOver){
                    jump();
                }else{
                    SceneManager.ACTIVE_SCENE = Constants.MENU_SCENE;
                    mScoreTimer.cancel();
                    mIsTimerStarted = false;
                    newGame();
                }
            }
        }
    }

    /**
     * Sets the scene ready for a new game.
     */
    private void newGame(){
        mGravityManager = new GravityManager();
        mObstacleManager = new ObstacleManager(300, 100, 100, Color.BLUE);
        mHealthManager = new HealthManager();
        mCloudManager = new CloudManager();

        mPauseButton = new PauseButton();

        mPlayer = new Player(new Rect(0, 0, 100, 100), Color.BLACK);
        mPlayerPoint = new Point(X_POSITION, Constants.SCREEN_HEIGHT/2);

        mFloor = new Floor(new Rect());

        mGravity = 0;
        mIsJumping = true;
        mDoubleJumpAvailable = true;
        mAllowedToJump = false;

        mGameOver = false;
        mIsPaused = false;

        mAmountOfDamage = 0;
        mScore = 0;

        mScoreTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(!mGameOver && !mIsPaused)
                    mScore++;
            }
        };
        mScoreTimer = new Timer(true);

        initMusic();
        initJumpSound();
        initBackground(); // Initialize the background image

        mScoreTimer.scheduleAtFixedRate(mScoreTimerTask, UPDATE_TIMER_INTERVAL, UPDATE_TIMER_INTERVAL);
        mIsTimerStarted = true;
    }

    /**
     * Takes the given text and draws it in the center of the screen.
     * @param canvas
     * @param paint
     * @param textOne
     * @param textTwo
     */
    private void drawCenterText(Canvas canvas, Paint paint, String textOne, String textTwo, String textThree){
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.getClipBounds(mTextRect);
        int cHeight = mTextRect.height();
        int cWidth = mTextRect.width();
        paint.getTextBounds(textOne, 0, textOne.length(), mTextRect);
        float x = cWidth / 2f;
        float y = cHeight / 2f - 100;
        canvas.drawText(textOne, x, y, paint);
        y = cHeight / 2f;
        canvas.drawText(textTwo, x, y, paint);
        y = cHeight / 2f + 100;
        canvas.drawText(textThree, x, y, paint);
    }

    @Override
    public void setHighScore(int highScore) {
        mHighScore = highScore;
    }
}