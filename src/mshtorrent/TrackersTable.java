/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

/**
 *
 * @author root
 */
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TrackersTable {

    TableView<TableEntryTrackers> table = new TableView<TableEntryTrackers>();

    public TrackersTable() {
        Scene scene = new Scene(new Group());
        table.setEditable(true);

        TableColumn hashCol = new TableColumn("#");
        hashCol.setMinWidth(100);

        TableColumn urlCol = new TableColumn("URL");
        urlCol.setMinWidth(250);

        TableColumn statusCol = new TableColumn("Status");
        statusCol.setMinWidth(100);

        TableColumn recievedCol = new TableColumn("Received");
        recievedCol.setMinWidth(100);

        TableColumn seedsCol = new TableColumn("Seeds");
        seedsCol.setMinWidth(100);

        TableColumn peersCol = new TableColumn("peers");
        peersCol.setMinWidth(100);

        TableColumn downloadedCol = new TableColumn("Downloaded");
        downloadedCol.setMinWidth(100);
        table.getColumns().addAll(
                hashCol,
                urlCol,
                statusCol,
                recievedCol,
                seedsCol,
                peersCol,
                downloadedCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

    }
}
