package edu.vanier.spaceshooter.controllers;

import edu.vanier.spaceshooter.models.Sprite;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class of the MainApp UI.
 */
public class MainAppFXMLController {

    private final static Logger logger = LoggerFactory.getLogger(MainAppFXMLController.class);
    @FXML
    private Pane animationPanel;
    private double elapsedTime = 0;
    private Sprite spaceShip;
    private Scene mainScene;
    AnimationTimer gameLoop;

    @FXML
    public void initialize() {
        logger.info("Initializing MainAppController...");
        spaceShip = new Sprite(300, 750, 40, 40, "player", Color.BLUE);
    }

    public void initGameComponents() {
        createContent();
        
        
        this.mainScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    spaceShip.moveLeft();
                    break;
                case D:
                    spaceShip.moveRight();
                    break;
                case SPACE:
                    shoot(spaceShip);
                    break;
            }
        });
    }

    private void createContent() {
        animationPanel.setPrefSize(600, 800);
        animationPanel.getChildren().add(spaceShip);
        //-- Create the game loop.
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
        nextLevel();
    }

    private void nextLevel() {
        for (int i = 0; i < 5; i++) {
            Sprite invader = new Sprite(90 + i * 100, 150, 30, 30, "enemy", Color.RED);
            animationPanel.getChildren().add(invader);
        }
    }

    private List<Sprite> sprites() {
        return animationPanel.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
    }

    private void update() {
        elapsedTime += 0.016;

        sprites().forEach((Sprite sprite) -> {
            switch (sprite.getType()) {
                case "enemybullet" -> {
                    sprite.moveDown();
                    // Check if an enemy bullet is colliding with the spaceship.
                    if (sprite.getBoundsInParent().intersects(spaceShip.getBoundsInParent())) {
                        spaceShip.setDead(true);
                        sprite.setDead(true);
                    }
                }
                case "playerbullet" -> {
                    sprite.moveUp();
                    sprites().stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
                        // Check if the player buller is colliding with an enemy.
                        if (sprite.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.setDead(true);
                            sprite.setDead(true);
                        }
                    });
                }
                case "enemy" -> {
                    if (elapsedTime > 2) {
                        if (Math.random() < 0.3) {
                            shoot(sprite);
                        }
                    }
                }
            }
        });

        animationPanel.getChildren().removeIf(n -> {
            Sprite sprite = (Sprite) n;
            return sprite.isDead();
        });

        if (elapsedTime > 2) {
            elapsedTime = 0;
        }
    }

    private void shoot(Sprite firingEntity) {
        // The firing entity can be either an enemy or the sapceship.
        Sprite bullet = new Sprite((int) firingEntity.getTranslateX() + 20, (int) firingEntity.getTranslateY(), 5, 20, firingEntity.getType() + "bullet", Color.BLACK);
        animationPanel.getChildren().add(bullet);
    }

    public void setScene(Scene scene) {
        this.mainScene = scene;
    }

    public void stopAnimation() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
}
