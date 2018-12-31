package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("葫芦娃大战妖精");
			Battlefield b = new Battlefield();
			GridPane root = new GridPane();
			MyCanvas mc = b.getMyCanvas();
			root.getChildren().add(mc);
			primaryStage.setScene(new Scene(root));
			b.setEvent();
	        primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
