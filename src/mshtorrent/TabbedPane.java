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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class TabbedPane {

    TabPane tabPane;

    TabbedPane() {

        Group root = new Group();
        Scene scene = new Scene(root, 400, 250, Color.WHITE);

        tabPane = new TabPane();

        BorderPane borderPane = new BorderPane();
        // for (int i = 0; i < 5; i++) 
        Tab general = new Tab();
        general.setText("General");
        HBox hbox1 = new HBox();
        hbox1.getChildren().add(new Label("General"));
        //hbox1.setAlignment();
        general.setContent(hbox1);

        Tab trackers = new Tab();
        trackers.setText("Trackers");
        
        HBox hbox2 = new HBox();
        TrackersTable tt=new TrackersTable();
        hbox2.getChildren().addAll(tt.table);
         HBox.setHgrow(tt.table, Priority.ALWAYS);
       // hbox2.setAlignment(Pos.CENTER);
        trackers.setContent(hbox2);

        Tab peers = new Tab();
        peers.setText("Peers");
        PeersTable pt=new PeersTable();
        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(pt.table);
        HBox.setHgrow(pt.table, Priority.ALWAYS);
       // hbox3.setAlignment(Pos.CENTER);
        peers.setContent(hbox3);
        

        Tab http_sources = new Tab();
        http_sources.setText("Http sources");
        HBox hbox4 = new HBox();
        hbox4.getChildren().add(new Label("Http sources"));
        hbox4.setAlignment(Pos.CENTER);
        http_sources.setContent(hbox4);

        Tab content = new Tab();
        content.setText("Content");
        HBox hbox5 = new HBox();
        hbox5.getChildren().add(new Label("content"));
        hbox5.setAlignment(Pos.CENTER);
        content.setContent(hbox5);

        Tab speed = new Tab();
        speed.setText("Speed");
        HBox hbox6 = new HBox();
        hbox6.getChildren().add(new Label("Speed"));
        hbox6.setAlignment(Pos.CENTER);
        speed.setContent(hbox6);

        tabPane.getTabs().addAll(general, trackers, peers, http_sources, content, speed);

        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);

    }

}
