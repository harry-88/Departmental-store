package sample;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StockHistory {

    private String FILENAME = "D:\\Items.txt";

    private Stage stageHistory;
    private AnchorPane apHistory;
    private Scene sceneHistory;
    private BorderPane bpHistory;
    private TableView<ItemHistory> tableItemHistory;
    private TableColumn tableColumnItemDateOfPurchase;
    private TableColumn tableColumnItemDetails;
    private TableColumn tableColumnItemName;
    private TableColumn tableColumnItemQuantity;
    private TableColumn tableColumnItemWeight;
    private TableColumn tableColumnItemBuyPrice;
    private TableColumn tableColumnItemRetailPrice;

    public StockHistory() {
        stageHistory = new Stage();
        stageHistory.setTitle("Stock History");
        stageHistory.setHeight(700);
        stageHistory.setWidth(700);

        MenuBar menuBar = new MenuBar();
        Menu optionMenu = new Menu("Option");

        try {
            MenuItem stokeItem = new MenuItem("Stoke");
            stokeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stageHistory.close();
                    new Stock().start(new Stage());
                }
            });
            MenuItem closeMenu = new MenuItem("Close");
            closeMenu.setOnAction(e -> stageHistory.close());
            optionMenu.getItems().addAll(stokeItem,closeMenu);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        menuBar.getMenus().addAll(optionMenu);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        apHistory = new AnchorPane();
        borderPane.setLeft(apHistory);
        stageHistory.setScene(new Scene(borderPane));
        stageHistory.show();
        tableStockHistory();
    }

    public void tableStockHistory() {
        tableItemHistory= new TableView<>();
        tableItemHistory.setLayoutX(10);
        tableItemHistory.setLayoutY(200);
        tableItemHistory.setPrefWidth(650);
        tableItemHistory.setPrefHeight(400);

        Label findLabel = new Label("Find from history");
        findLabel.setLayoutX(10);
        findLabel.setLayoutY(20);

        TextField findField = new TextField();
        findField.setLayoutX(10);
        findField.setLayoutY(50);
        findField.setStyle("-fx-background-radius: 20px");

        Button btnFind = new Button("Find");
        btnFind.setLayoutX(10);
        btnFind.setLayoutY(100);
        btnFind.setStyle("-fx-background-radius: 20px");

        Label emptyLabel = new Label("Field is required");
        emptyLabel.setLayoutX(10);
        emptyLabel.setLayoutY(80);

        btnFind.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tableItemHistory.getItems().size() == 0)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"We have not any thing in table to find");
                    alert.showAndWait();

                }
                else if (findField.getText().isEmpty())
                {
                    apHistory.getChildren().add(emptyLabel);
                }
                else
                {
                    apHistory.getChildren().remove(emptyLabel);
                    ItemHistory itemHistory = null;
                    for (int i = 0;i<tableItemHistory.getItems().size();i++)
                    {
                        itemHistory = tableItemHistory.getItems().get(i);
                        if (findField.getText().contains(itemHistory.getItemDateOfPurchase()) || findField.getText().contains(itemHistory.getItemName()) || findField.getText().contains(itemHistory.getItemWeight())
                                || findField.getText().contains(itemHistory.getItemQuantity()) || findField.getText().contains(itemHistory.getItemRetailPrice()) || findField.getText().contains(itemHistory.getItemBuyPrice()))

                        {
                            tableItemHistory.getItems().remove(i);
                            tableItemHistory.getItems().add(0,itemHistory);
                        }
                    }
                    findField.clear();
                }
            }
        });

        apHistory.getChildren().addAll(findLabel,findField,btnFind);

        tableColumnItemDateOfPurchase = new TableColumn("Date of Purchase");
        tableColumnItemDetails = new TableColumn("Item Details");
        tableColumnItemName = new TableColumn("Item Name");
        tableColumnItemQuantity = new TableColumn("Item Quantity");
        tableColumnItemWeight= new TableColumn("Item Weight");
        tableColumnItemRetailPrice = new TableColumn("Item Price");
        tableColumnItemBuyPrice = new TableColumn("Item Buy Price");
        tableColumnItemDetails.getColumns().addAll(tableColumnItemName, tableColumnItemQuantity,
                tableColumnItemWeight, tableColumnItemRetailPrice, tableColumnItemBuyPrice);

        tableColumnItemDateOfPurchase.setPrefWidth(150);
        tableColumnItemName.setPrefWidth(100);
        tableColumnItemQuantity.setPrefWidth(100);
        tableColumnItemWeight.setPrefWidth(100);
        tableColumnItemRetailPrice.setPrefWidth(100);
        tableColumnItemBuyPrice.setPrefWidth(100);

        tableItemHistory.getColumns().addAll(tableColumnItemDateOfPurchase,tableColumnItemDetails);
        apHistory.getChildren().add(tableItemHistory);
        try {
            addItemInTable();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void addItemInTable() throws IOException {
        FileReader fr=null;
        BufferedReader br=null;
        fr = new FileReader(FILENAME);
        br = new BufferedReader(fr);
        String line=null;
        String token[]= {};
        while((line = br.readLine())!=null){
            token=line.split(",");
            tableItemHistory.getItems().add(new ItemHistory(token[8],token[0],token[1],token[3],token[2],token[7]));

            tableColumnItemDateOfPurchase.setCellValueFactory(new PropertyValueFactory<Item, SimpleStringProperty>("itemDateOfPurchase"));
            tableColumnItemName.setCellValueFactory(new PropertyValueFactory<Item, SimpleStringProperty>("itemName"));
            tableColumnItemQuantity.setCellValueFactory(new PropertyValueFactory<Item, SimpleStringProperty>("itemQuantity"));
            tableColumnItemWeight.setCellValueFactory(new PropertyValueFactory<Item, SimpleStringProperty>("itemWeight"));
            tableColumnItemRetailPrice.setCellValueFactory(new PropertyValueFactory<Item, SimpleStringProperty>("itemRetailPrice"));
            tableColumnItemBuyPrice.setCellValueFactory(new PropertyValueFactory<Item, SimpleStringProperty>("itemBuyPrice"));
        }

    }

}