package com.example.demo.gameboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.net.URL;

public class Gameboard extends Application {
    // 🔹 Grid constants
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int SCENE_WIDTH = 820;
    private static final int SCENE_HEIGHT = 800;
    private static int playerX = 1;
    private static int playerY = 1;
    private static int lives = 3;

    enum CellType {
        GRASS, PLAYER, PRINCESS, BOMB, WALL
    }

    // 🔹 Use "matrix" instead of "map"
    private CellType[][] matrix = new CellType[ROWS][COLS];

    @Override
    public void start(Stage stage) {

        initMatrix();
            GridPane grid = new GridPane();

            BorderPane root = new BorderPane();
            root.setCenter(grid);
            Scene scene = new Scene(root, SCENE_WIDTH,SCENE_HEIGHT);
            drawBoard(grid);

            stage.setTitle("Rescue the Princess");
            stage.setScene(scene);
            stage.show();
            scene.setOnKeyPressed(event -> {
                switch (event.getCode()){
                    case DOWN -> {
                        movePlayer(1,0);
                        drawBoard(grid);
                        isAlive();
                    }
                    case RIGHT -> {
                        movePlayer(0,1);
                        drawBoard(grid);
                        isAlive();
                    }
                    case LEFT -> {
                        movePlayer(0, -1);
                        drawBoard(grid);
                        isAlive();
                    }
                    case UP -> {
                        movePlayer(-1, 0);
                        drawBoard(grid);
                        isAlive();
                    }
                }
            });


    }
    private void isAlive(){
        if(lives<= 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wasted");
            alert.setContentText("You Lost!");
            alert.setHeaderText(null);
            alert.showAndWait();
            System.exit(0);}
    }
    private void movePlayer(int x, int y){
        if (playerX+x != 0 && playerX+x != 9 && playerY+y != 0 && playerY+y != 9) {
            matrix[playerX][playerY] = CellType.GRASS;
            playerX += x;
            playerY += y;

            if (matrix[playerX][playerY] == CellType.PRINCESS){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Victory!");
                alert.setContentText("You rescued the princess!");
                alert.setHeaderText(null);
                alert.showAndWait();
                System.exit(0);
            }else if(matrix[playerX][playerY] == CellType.BOMB){
                lives --;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Boom!");
                alert.setContentText("You stepped on a bomb! Lives remaining: "+lives);
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            matrix[playerX][playerY] = CellType.PLAYER;
        }
    }

    private void initMatrix() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                matrix[r][c] = CellType.GRASS;
                matrix[0][c] = CellType.WALL;
                matrix[r][0] = CellType.WALL;
                matrix[9][c] = CellType.WALL;
                matrix[r][9] = CellType.WALL;
            }
        }
        // Collect all empty inner cells
        List<int[]> freeCells = new ArrayList<>();
        for (int r = 1; r < ROWS - 1; r++) {
            for (int c = 1; c < COLS - 1; c++) {
                if (matrix[r][c] == CellType.GRASS) {
                    freeCells.add(new int[]{r, c});
                }
            }
        }

        // Shuffle and place princess and bombs in random positions
        Collections.shuffle(freeCells);
        matrix[freeCells.get(0)[0]][freeCells.get(0)[1]] = CellType.PRINCESS;
        matrix[freeCells.get(1)[0]][freeCells.get(1)[1]] = CellType.BOMB;
        matrix[freeCells.get(2)[0]][freeCells.get(2)[1]] = CellType.BOMB;
        matrix[freeCells.get(3)[0]][freeCells.get(3)[1]] = CellType.BOMB;
        matrix[freeCells.get(4)[0]][freeCells.get(4)[1]] = CellType.BOMB;
        matrix[freeCells.get(5)[0]][freeCells.get(5)[1]] = CellType.BOMB;

        matrix[playerX][playerY] = CellType.PLAYER;
    }

    private void drawBoard(GridPane grid) {
        grid.getChildren().clear();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = new StackPane();
                cell.setPrefSize(80, 80);
                cell.setStyle("-fx-border-color: black; -fx-background-color: beige;");



                URL pictureUrl = this.getClass().getResource("grass.png");





                Label label = new Label();

                if(matrix[row][col] == CellType.PLAYER ) {
                    pictureUrl = this.getClass().getResource("player.png");
                }else if(matrix[row][col] == CellType.PRINCESS ) {
                    pictureUrl = this.getClass().getResource("princess.png");
                }else if(matrix[row][col] == CellType.BOMB){
                    pictureUrl = this.getClass().getResource("bomb.png");
                }else if(matrix[row][col] == CellType.WALL){
                    pictureUrl = this.getClass().getResource("wall.png");
                }else{
                    label.setText("");
                }

                Image image = new Image(String.valueOf(pictureUrl));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                cell.getChildren().add(imageView);
                grid.add(cell, col, row);
            }
        }
    }


}