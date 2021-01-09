/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

/**
 *
 * @author saumyachaudhary
 */
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.awt.Panel;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.*;

public class MshTorrent extends Application {

    @Override

    public void start(Stage stage) {
        HBox hbox = new HBox(20);
        hbox.setTranslateX(8);
        hbox.setTranslateY(8);

        SplitPane splitPane1 = new SplitPane();
        ScrollPane scrollPane = new ScrollPane();

        Table obj3 = new Table();

        TabbedPane obj4 = new TabbedPane();

        final VBox vbox = new VBox();
        VBox.setVgrow(obj3.table, Priority.ALWAYS);
        vbox.setStyle("-fx-background-color: blue");
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(obj3.table);

        scrollPane.setContent(vbox);
        // splitPane1.setOneTouchExpandable(true);
        splitPane1.setOrientation(Orientation.VERTICAL);
        splitPane1.setPrefSize(1500, 400);

        // splitPane1.setTopComponent(vbox);
        // final Button l1 = new Button("Left Button");
        // final Button r1 = new Button("Right Button");
        splitPane1.getItems().addAll(scrollPane, obj4.tabPane);
        hbox.getChildren().add(splitPane1);

        Image icon = new Image(getClass().getResourceAsStream("/images/download.png"));

        BorderPane pane = new BorderPane();
        VBox topContainer = new VBox();
        topContainer.setPrefWidth(100);
        TorrentMenu obj = new TorrentMenu();
        TorrentToolbar obj1 = new TorrentToolbar();
        SideMenu obj2 = new SideMenu();

        topContainer.getChildren().add(obj.menuBar);
        topContainer.getChildren().add(obj1.toolBar1);

        pane.setTop(topContainer);
        pane.setLeft(obj2.toolBar2);
        pane.setCenter(hbox);
        stage.setTitle("Torrent Application");
        //Scene scene = new Scene(new Group(hbox), 560, 240);
        Scene scene = new Scene(pane, 600, 400);
        stage.getIcons().add(icon);
        scene.setFill(Color.PINK);
        stage.setScene(scene);

        stage.show();
        AllTorrentHandler.loadTorrentsFromDatabase();
        System.out.println("MSH torrent started..");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
