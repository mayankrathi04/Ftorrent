package mshtorrent;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableRow;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Callback;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
 import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.cell.ProgressBarTableCell;
public class Table {

    static TableView<TableEntry> table = new TableView<TableEntry>();
    static ObservableList<TableEntry> data =FXCollections.observableArrayList(); 
    
    public Table() {

        // TableView<TableEntry> table = new TableView<TableEntry>();
               /*
                 new Person("Jacob", "Smith", "jacob.smith@example.com"),
         new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
         new Person("Ethan", "Williams", "ethan.williams@example.com"),
         new Person("Emma", "Jones", "emma.jones@example.com"),
         new Person("Michael", "Brown", "michael.brown@example.com")
         );*/
        Scene scene = new Scene(new Group());
        //stage.setTitle("Table View Sample");
        // stage.setWidth(450);
        //stage.setHeight(500);

        //final Label label = new Label("Address Book");
        //label.setFont(new Font("Arial", 20));
        table.setEditable(true);

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("name"));

        TableColumn sizeCol = new TableColumn("Size(In Gb)");
        sizeCol.setMinWidth(100);
        sizeCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("size"));
       // ProgressIndicator doneProg=new ProgressIndicator();
        ProgressBar doneProgress = new ProgressBar();
        TableColumn doneCol = new TableColumn("Done");
        doneCol.setMinWidth(100);
        doneCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,Double>("done"));
        doneCol.setCellFactory(ProgressBarTableCell.<Table.TableEntry> forTableColumn());        
        table.setItems(data);
        TableColumn statusCol = new TableColumn("Status");
        statusCol.setMinWidth(100);
        statusCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("status"));

        TableColumn seedsCol = new TableColumn("Seeds");
        seedsCol.setMinWidth(100);
        seedsCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("seeds"));

        TableColumn peersCol = new TableColumn("Peers");
        peersCol.setMinWidth(100);
        peersCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("peers"));

        TableColumn speedDownCol = new TableColumn("Speed Down");
        speedDownCol.setMinWidth(50);
        speedDownCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("speedDown"));

        TableColumn speedUpCol = new TableColumn("Speed Up");
        speedUpCol.setMinWidth(50);
        speedUpCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("speedUp"));

        TableColumn etaCol = new TableColumn("ETA");
        etaCol.setMinWidth(100);
        etaCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("ETA"));

        TableColumn uploadedCol = new TableColumn("Uploaded");
        uploadedCol.setMinWidth(100);
        uploadedCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("uploaded"));

        TableColumn ratioCol = new TableColumn("Ratio");
        ratioCol.setMinWidth(100);
        ratioCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("ratio"));

        TableColumn availableCol = new TableColumn("Available");
        availableCol.setMinWidth(100);
        availableCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("available"));

        TableColumn labelCol = new TableColumn("Label");
        labelCol.setMinWidth(100);
        labelCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("label"));

        TableColumn addedOnCol = new TableColumn("Added On ");
        addedOnCol.setMinWidth(100);
        addedOnCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("addedOn"));

        TableColumn completedOnCol = new TableColumn("Completed On");
        completedOnCol.setMinWidth(100);
        completedOnCol.setCellValueFactory(new PropertyValueFactory<Table.TableEntry,String>("completedOn"));

        table.getColumns().addAll(
                nameCol,
                sizeCol,
                doneCol,
                statusCol,
                seedsCol,
                peersCol,
                speedDownCol,
                speedUpCol,
                etaCol,
                uploadedCol,
                ratioCol,
                availableCol,
                labelCol,
                addedOnCol,
                completedOnCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        //  vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        //For right click menu
        table.setRowFactory(new Callback<TableView<Table.TableEntry>, TableRow<Table.TableEntry>>() {
            @Override
            public TableRow<Table.TableEntry> call(TableView<Table.TableEntry> tableView) {
                final TableRow<Table.TableEntry> row = new TableRow<Table.TableEntry>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeItem = new MenuItem("Remove");
                removeItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                     //  AllTorrentHandler.removeTorrentByName(row.getItem().getName());
                        table.getItems().remove(row.getItem());
                    }
                });
                contextMenu.getItems().add(removeItem);
                final MenuItem pauseItem = new MenuItem("Pause");
                pauseItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        table.getItems().remove(row.getItem());
                    }
                });
                contextMenu.getItems().add(pauseItem);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }
        });

    }
    public void addRow(Table.TableEntry newTor) {
        table.getItems().add(newTor);
    }

    public void removeRow(Table.TableEntry tor) {
        table.getItems().remove(tor);
    }

    public static class TableEntry {

        private SimpleStringProperty name;
        private SimpleDoubleProperty size;
        private SimpleDoubleProperty done;
        private SimpleStringProperty status;
        private SimpleStringProperty seeds;
        private SimpleStringProperty peers;
        private SimpleStringProperty speedUp;
        private SimpleStringProperty speedDown;
        private SimpleStringProperty ETA;
        private SimpleStringProperty uploaded;
        private SimpleStringProperty ratio;
        private SimpleStringProperty available;
        private SimpleStringProperty label;
        private SimpleStringProperty addedOn;
        private SimpleStringProperty completedOn;

         TableEntry(String name1, double size1, double done1, String status1, String seeds1, String peers1, String speedUp1,
                String speedDown1, String ETA1, String uploaded1, String ratio1, String available1,
                String label1, String addedOn1, String completedOn1) {
            this.name = new SimpleStringProperty(name1);
            this.size = new SimpleDoubleProperty(size1/(1024*1024*1024));
            this.done = new SimpleDoubleProperty(done1);
            this.status = new SimpleStringProperty(status1);
            this.seeds = new SimpleStringProperty(seeds1);
            this.peers = new SimpleStringProperty(peers1);
            this.speedUp = new SimpleStringProperty(speedUp1);
            this.speedDown = new SimpleStringProperty(speedDown1);
            this.ETA = new SimpleStringProperty(ETA1);
            this.uploaded = new SimpleStringProperty(uploaded1);
            this.ratio = new SimpleStringProperty(ratio1);
            this.available = new SimpleStringProperty(available1);
            this.label = new SimpleStringProperty(label1);
            this.addedOn = new SimpleStringProperty(ETA1);
            this.completedOn = new SimpleStringProperty(uploaded1);

        }

        public String getName() {
            return(name.get());
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public double getSize() {
            return (size.get());
        }

        public void setSize(double size) {
            this.size.set(size);
        }

        public double getDone() {
            return (done.get());
        }

        public void setDone(double done) {
            this.done.set(done);
        }

        public String getStatus() {
            return (status.get());
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public String getSeeds() {
            return (seeds.get());
        }

        public void setSeeds(String seeds) {
            this.seeds.set(seeds);
        }

        public String getPeers() {
            return (peers.get());
        }

        public void setPeers(String peers) {
            this.peers.set(peers);
        }

        public String getSpeedUp() {
            return (speedUp.get());
        }

        public void setSpeedUp(String speedUp) {
            this.speedUp.set(speedUp);
        }

        public String getSpeedDown() {
            return speedDown.get();
        }

        public void setSpeedDown(String speedDown) {
            this.speedDown.set(speedDown);
        }

        public String getETA() {
            return ETA.get();
        }

        public void setETA(String ETA) {
            this.ETA.set(ETA);
        }

        public String getUploaded() {
            return uploaded.get();
        }

        public void setUploaded(String uploaded) {
            this.uploaded.set(uploaded);
        }

        public String getRatio() {
            return ratio.get();
        }

        public void setRatio(String ratio) {
            this.ratio.set(ratio);
        }

        public String getAvailable() {
            return available.get();
        }

        public void setAvailable(String available) {
            this.available.set(available);
        }

        public String getLabel() {
            return label.get();
        }

        public void setLabel(String label) {
            this.label.set(label);
        }

        public String getAddedOn() {
            return addedOn.get();
        }

        public void setAddedOn(String addedOn) {
            this.addedOn.set(addedOn);
        }

        public String getCompletedOn() {
            return completedOn.get();
        }

        public void setCompletedOn(String completedOn) {
            this.completedOn.set(completedOn);
        }

    }
}
