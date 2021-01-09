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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TorrentMenu extends Application {

    Stage window;
    BorderPane layout;
    Menu fileMenu, editMenu, helpMenu, viewMenu, windowMenu, submenu;
    MenuBar menuBar;
    MenuItem menuItem, open;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        layout = new BorderPane();
        layout.setTop(menuBar);
        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.show();
    }

    public TorrentMenu() {
        FileChooser fileChooser = new FileChooser();

        //menu started
        fileMenu = new Menu("File");

        menuItem = new MenuItem("New Torrent");
        fileMenu.getItems().add(menuItem);
        open = new MenuItem("Open Torrent");
        //file chooser
        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(window);
                if (file != null) {
                    String filepath = file.getAbsolutePath();
                    System.out.println(filepath);
                    TorrentFile ob = new TorrentFile(filepath);
                    AllTorrentHandler.addTorrent(ob);
                }
            }
        }
        );
        fileMenu.getItems().add(open);

        menuItem = new MenuItem("Open Torrent from URL");
        fileMenu.getItems().add(menuItem);

        menuItem = new MenuItem("Add RSS feed");
        fileMenu.getItems().add(menuItem);
        fileMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Close Window");
        fileMenu.getItems().add(menuItem);
        fileMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Quick look");
        fileMenu.getItems().add(menuItem);

        //edit
        editMenu = new Menu("Edit");

        menuItem = new MenuItem("Cut");
        editMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Copy");
        editMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Paste");
        editMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Delete");
        editMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Select All");
        editMenu.getItems().add(menuItem);
        editMenu.getItems().add(new SeparatorMenuItem());
        submenu = new Menu("Speak");
        menuItem = new MenuItem("Start Speaking");
        submenu.getItems().add(menuItem);
        menuItem = new MenuItem("Stop Speaking");
        submenu.getItems().add(menuItem);
        editMenu.getItems().add(submenu);
        editMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Start Dictation");
        editMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Emoji & Symbol");
        editMenu.getItems().add(menuItem);

        //view
        viewMenu = new Menu("View");

        menuItem = new MenuItem("Show Tab Bar");
        viewMenu.getItems().add(menuItem);
        viewMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Hide Sidebar");
        viewMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Hide Details Pane");
        viewMenu.getItems().add(menuItem);
        viewMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Hide Toolbar");
        viewMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Customize Toolbar");
        viewMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Hide Full Screen");
        viewMenu.getItems().add(menuItem);

        //window
        windowMenu = new Menu("Window");

        menuItem = new MenuItem("Minimize");
        windowMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Zoom");
        windowMenu.getItems().add(menuItem);
        windowMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Show Previous Tab");
        windowMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Show Next Tab");
        windowMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Move Tab To The Window");
        windowMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Merge All Windows");
        windowMenu.getItems().add(menuItem);
        windowMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("BitTorrent");
        windowMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Message Log");
        windowMenu.getItems().add(menuItem);
        windowMenu.getItems().add(new SeparatorMenuItem());
        menuItem = new MenuItem("Bring All To Front");
        windowMenu.getItems().add(menuItem);

        //help
        helpMenu = new Menu("Help");

        //JPanel panel1 = new JPanel(); // set frame layout
        // TextField Search;
        //Search=new TextField("");
        // menuItem = new MenuItem(Search);
        //panel1.add(menuItem);
        //panel1.add(jSearch);
        //menu.add(panel1);?
        // menu.add(menuItem);
        // helpMenu.getItems().add(menuItem);
        menuItem = new MenuItem("BitTorrent Homepage");
        helpMenu.getItems().add(menuItem);
        menuItem = new MenuItem("User Forums");
        helpMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Report an Issue");
        helpMenu.getItems().add(menuItem);
        menuItem = new MenuItem("Ad-Free Account");
        helpMenu.getItems().add(menuItem);

        //Main menu bar
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, windowMenu, helpMenu);

    }

    public static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Chooose torrent");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

}
