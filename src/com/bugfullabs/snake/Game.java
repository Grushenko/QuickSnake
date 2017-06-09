package com.bugfullabs.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {


    private Snake.Direction mDirection;
    private Scene mScene;
    private Board mBoard;
    private Snake mSnake;
    private Timer mLogicTimer;
    private AnimationTimer mAnimationTimer;
    private boolean mElongate;
    private int mAppleX;
    private int mAppleY;

    Game() {
        mElongate = false;
        mDirection = Snake.Direction.UP;
        mBoard = new Board(30, 30);
        VBox box = new VBox(mBoard);
        mScene = new Scene(box);

        mSnake = new Snake(10, 10, 4);
        generateApple();

        mLogicTimer = new Timer();
        mLogicTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean collision = mSnake.step(mDirection, mElongate);
                if (collision ||
                        mSnake.getX() >= 30 || mSnake.getX() < 0 ||
                        mSnake.getY() >= 30 || mSnake.getY() < 0) {
                    mLogicTimer.cancel();
                    Platform.runLater(()-> {
                        Alert a = new Alert(Alert.AlertType.WARNING);
                        a.setHeaderText("UPS");
                        a.setContentText("GAME OVER");
                        a.showAndWait();
                    });
                }

                if (mElongate)
                    mElongate = false;
                if (mSnake.isCollision(mAppleX, mAppleY)) {
                    mElongate = true;
                    generateApple();
                }
            }
        }, 0, 200);

        mAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long pL) {
                mBoard.drawSnake(mSnake);
                mBoard.drawApple(mAppleX, mAppleY);
            }
        };

        mAnimationTimer.start();

        mScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                    mDirection = Snake.Direction.UP;
                    break;
                case DOWN:
                    mDirection = Snake.Direction.DOWN;
                    break;
                case LEFT:
                    mDirection = Snake.Direction.LEFT;
                    break;
                case RIGHT:
                    mDirection = Snake.Direction.RIGHT;
                    break;

            }
        });
    }

    private void generateApple() {
        Random rnd = new Random();
        mAppleX = rnd.nextInt(30);
        mAppleY = rnd.nextInt(30);
    }

    public Scene getScene() {
        return mScene;
    }
}