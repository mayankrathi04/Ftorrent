/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author saumyachaudhary
 */
public class SideMenu {

    ToolBar toolBar2;

    public SideMenu() {
        Image imageAll = new Image(getClass().getResourceAsStream("/images/all.png"));

        Image imageDownloading = new Image(getClass().getResourceAsStream("/images/downloading.png"));
        Image imageCompleted = new Image(getClass().getResourceAsStream("/images/completed.png"));
        Image imageActive = new Image(getClass().getResourceAsStream("/images/active.png"));

        Image imageInactive = new Image(getClass().getResourceAsStream("/images/inactive.png"));

        Button btnall = new Button("All", new ImageView(imageAll));
        Button btndownloading = new Button("Downloading", new ImageView(imageDownloading));
        Button btncompleted = new Button("Completed", new ImageView(imageCompleted));
        Button btnactive = new Button("Active", new ImageView(imageActive));
        Button btninactive = new Button("Inactive", new ImageView(imageInactive));

        btnall.setPrefSize(100, 20);
        btndownloading.setPrefSize(100, 20);
        btncompleted.setPrefSize(100, 20);
        btnactive.setPrefSize(100, 20);
        btninactive.setPrefSize(100, 20);

        toolBar2 = new ToolBar();
        toolBar2.setOrientation(Orientation.VERTICAL);
        toolBar2.getItems().addAll(
                new Separator(),
                btnall, btndownloading, btncompleted, btnactive, btninactive,
                new Separator()
        );
    }
}
