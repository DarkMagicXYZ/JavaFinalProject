package main;


import javafx.scene.text.Font;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MyCanvas extends Canvas {
	public GraphicsContext gc;
	private Image background = new Image("Battlefield.png");
	public MyCanvas(){
		super(900, 700);
		gc = getGraphicsContext2D();
		gc.drawImage(background,0,0);
		gc.setFont(Font.font("Arial",38));
	}
}
