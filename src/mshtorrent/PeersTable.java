package mshtorrent;

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

public class PeersTable {

    TableView<TableEntryPeers> table = new TableView<TableEntryPeers>();

    public PeersTable() {

        Scene scene = new Scene(new Group());
        table.setEditable(true);

        TableColumn countryCol = new TableColumn("Country");
        countryCol.setMinWidth(100);
        

        TableColumn ipCol = new TableColumn("IP");
        ipCol.setMinWidth(100);
      

        TableColumn portCol = new TableColumn("Port");
        portCol.setMinWidth(100);
      
        TableColumn speedDownCol = new TableColumn("Speed Down");
        speedDownCol.setMinWidth(50);

        TableColumn speedUpCol = new TableColumn("Speed Up");
        speedUpCol.setMinWidth(50);

        TableColumn downloadedCol = new TableColumn("Downloaded");
        downloadedCol.setMinWidth(100);

        table.getColumns().addAll(
                countryCol,
                ipCol,
                portCol,
                speedDownCol,
                speedUpCol,
                downloadedCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        //  vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

    }
}