package mshtorrent;

/**
 *
 * @author saumyachaudhary
 */
import java.awt.Panel;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;

public class TorrentToolbar {

    ToolBar toolBar1;
    Stage window;
//Stage primaryStage;
 

    public TorrentToolbar() {
      /*    Pane leftSpacer = new Pane();
        HBox.setHgrow(
                leftSpacer,
                Priority.SOMETIMES
        );

         Pane rightSpacer = new Pane();
        HBox.setHgrow(
                rightSpacer,
                Priority.SOMETIMES
        );
*/
        Image imageRemove = new Image(getClass().getResourceAsStream("/images/remove.png"));

        Image imageStop = new Image(getClass().getResourceAsStream("/images/stop.png"));
        Image imageStart = new Image(getClass().getResourceAsStream("/images/start.png"));
        Image imageAdd = new Image(getClass().getResourceAsStream("/images/add.png"));

        Image imageAddFeed = new Image(getClass().getResourceAsStream("/images/addfeed.png"));
        Image imageUrl = new Image(getClass().getResourceAsStream("/images/addUrl.png"));
        Image imageUpgarde = new Image(getClass().getResourceAsStream("/images/upgrade.png"));
        
        Button btnAdd = new Button("Add", new ImageView(imageAdd));
                FileChooser fileChooser = new FileChooser();
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                TorrentMenu.configureFileChooser(fileChooser);
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
        // Label add = new Label("Add");
         //Group root = new Group();

    //root.getChildren().add(btnAdd);
   // root.getChildren().add(add);
        Button btnUrl = new Button("Add URL", new ImageView(imageUrl));
        Button btnFeed = new Button("Add Feed", new ImageView(imageAddFeed ));

        Button btnRemove = new Button("Remove", new ImageView(imageRemove));
        //btnRemove.setPrefSize(5, 5);
        Button btnStop = new Button("Stop", new ImageView(imageStop));
        Button btnStart = new Button("Start", new ImageView(imageStart));
        Button btnUpgrade = new Button("Upgrade Now", new ImageView(imageUpgarde));
        TextField search = new TextField("search");

        toolBar1 = new ToolBar();

        toolBar1.getItems().addAll(
                new Separator(),
                //root,
                btnAdd,
                btnUrl, btnFeed, new Separator(),
               //  leftSpacer,
                btnStart, btnStop, btnRemove, new Separator(),  
               // leftSpacer,
                btnUpgrade, search
        );
        

        /*
         BorderPane pane = new BorderPane();
         VBox topContainer = new VBox();
         TorrentMenu obj=new TorrentMenu();
         topContainer.getChildren().add(obj.menuBar);
         topContainer.getChildren().add(toolBar1);
        
         pane.setTop(topContainer);
         pane.setLeft(toolBar2);
         //  Scene scene = new Scene(pane, 600, 400);
         // primaryStage.setScene(scene);
         //primaryStage.setTitle("");
         // primaryStage.show();
       
         */
    }
}
