package sample;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Alert.*;


import java.io.File;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main extends Application {
    private String FILENAME ;
    private String FILETHATSTORECLIENTID = "D:\\ID.txt";
    private String TEMP = "D:\\Temp.txt";
    private String FILETHATSTOREHISTORYOFCLIENT = "D:\\Client.txt";
    
    private  MenuBar menuBar;

    private AnchorPane anchorPane;

    private Label remaining;
    private Label totalPrice;

    private Button btnDeleteRow;

    private TextField castomerBalance;
    private TextField itemNameField;
    private TextField itemQuantityField;
    private TextField weightField;
    private TextField serialNumberField;
    private TextField serialNumberQuantity;

    private Button btnAdd;
    private Button btnSerialNumber;

    private Button btnAddItemWithName;
    private MenuItem closeMenu;

    protected TableColumn column1;
    protected TableColumn column2;
    protected TableColumn column3;
    protected TableColumn column4;


    public TableView<AddItmForClient> table;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Data Enter");



        FILENAME = "D:\\Items.txt";

        makeMenus();//it make the menu on the top of the stage
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);

        anchorPane = new AnchorPane();



        enterData();//it make all the fields, labels and btns on the screen

        makeTable();//it make the table in which item will show

        borderPane.setLeft(anchorPane);
        Scene scene = new Scene(borderPane,1000,700);



        primaryStage.setScene(scene);
        closeMenu.setOnAction(e->primaryStage.close());

        primaryStage.show();
    }

    public void enterData ()throws Exception
    {


        Label  itemNameLabel = new Label("Item Name  : ");
        itemNameLabel.setLayoutX(10);
        itemNameLabel.setLayoutY(50);

        itemNameField = new TextField();
        itemNameField.setLayoutX(100);
        itemNameField.setLayoutY(45);
        itemNameField.setStyle("-fx-background-radius: 20px");
        itemNameField.setPrefWidth(100);

        Label itemQuantityLabel = new Label("Item Quantity : ");
        itemQuantityLabel.setLayoutY(50);
        itemQuantityLabel.setLayoutX(230);

        itemQuantityField = new TextField();
        itemQuantityField.setLayoutY(45);
        itemQuantityField.setLayoutX(320);
        itemQuantityField.setPrefWidth(100);
        itemQuantityField.setStyle("-fx-background-radius: 20px");

        Label weightLabel = new Label("Weight : ");
        weightLabel.setLayoutX(450);
        weightLabel.setLayoutY(50);

        weightField = new TextField();
        weightField.setLayoutY(45);
        weightField.setLayoutX(520);
        weightField.setPrefWidth(100);
        weightField.setStyle("-fx-background-radius: 20px");

        Button btnAddItemWithName = new Button("Add Item");
        //this button will add item on the table
        btnAddItemWithName.setLayoutY(45);
        btnAddItemWithName.setLayoutX(650);
        btnAddItemWithName.setStyle("-fx-background-radius: 30px");
        //following is the action that perform in time of click
        btnAddItemWithName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean emptyChecker = true;
                //this following condition check that the all fields is empty or not.
                if (itemNameField.getText().isEmpty() || itemQuantityField.getText().isEmpty() || weightField.getText().isEmpty())
                {

                    Alert alert = new Alert(AlertType.ERROR,"All fields are rquireds");
                    alert.setHeaderText("enter carefully");
                    alert.showAndWait();

                }
                else//this else is execute when the user enter all requierd fields.
                {
                    boolean flag = true;//it check that the item is present or not
                    try {
                        FileReader reader = new FileReader(FILENAME);//it read the file
                        BufferedReader bufferedReader = new BufferedReader(reader);//it help to read the file

                        String line = null;//make a variable of line that read the line from the file
                        String token[] = {};//it split the line that I read from the file
                        boolean checker  = true;//it help us to increase the quantity of the item if it present in table
                        line = bufferedReader.readLine();//read the line from the file
                        while(((line) != null) && checker)//this loop will check all the file until the file will finish or the the item is not entered into table
                        {
                            token = line.split(",");//it split the line.
                            if (itemNameField.getText().trim().equals(token[0]))//this condition check that item name that user enter is avilable in our file or not
                            {

                                if (Integer.parseInt(itemQuantityField.getText()) > Integer.parseInt(token[1]))//this condition check that the quantity tht user enter is available or not
                                {
                                    Alert alert = new Alert(AlertType.ERROR,"you enter large amount of quantity which we have not ");
                                    alert.showAndWait();
                                }
                                else if (!(weightField.getText().equals(token[3])))//this condition check that the weight that user enter is valid or not
                                {
                                    Alert alert = new Alert(AlertType.ERROR,"we have no any such item that have the weight that you entered");
                                    alert.showAndWait();
                                }
                                else//this else is use to add item in table
                                {

                                    double price = Double.parseDouble(token[2]);//it store the price that store in file
                                    int quantity = Integer.parseInt(itemQuantityField.getText());//it store the quantity that the user entered

                                    double total = Double.parseDouble(totalPrice.getText());//it take the total price that store in the label of totalPrice
                                    total =  (total + (price * quantity));//it calculate the new price
                                    totalPrice.setText(String.valueOf(total));//set the new value to total price label

                                    boolean isPresent = true;
                                    for (int i = 0;i<table.getItems().size();i++)//this loop will add quantity on the table if the item is already enter in table
                                    {
                                        AddItmForClient addItmForClient = table.getItems().get(i);//it get the data that present in table
                                        if (addItmForClient.getName().equals(token[0]) && addItmForClient.getWeight().equals(token[3]))//this condition check the name and waight of the item
                                        {
                                            double priceInTable = Double.parseDouble(addItmForClient.getPrice());

                                            priceInTable = priceInTable + price;//it get the price after add the other itens

                                            addItmForClient.setPrice(""+(price + priceInTable));//it set the price after adding the new items

                                            addItmForClient.setItemQuantity(""+(Integer.parseInt(addItmForClient.getItemQuantity()) + quantity));

                                            isPresent = false;//it use to enter a seperate item
                                        }
                                        if (!isPresent)
                                            break;//if the item quantity is increases then the loop will break.
                                    }
                                    if (isPresent == true)//when the variable is true the the item will add the item in the seperate item.
                                    {
                                        enterItem(new AddItmForClient(itemNameField.getText(), itemQuantityField.getText(), "" + (price * quantity), weightField.getText(),token[4],token[5],token[6],token[7]));

                                        column1.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("name"));
                                        column2.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("itemQuantity"));
                                        column4.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("price"));
                                        column3.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("weight"));
                                    }
                                    isPresent = true;

                                    //decrement in qunatity that store in file

                                    FileReader reader1 = new FileReader(FILENAME);
                                    BufferedReader bufferedReader1 = new BufferedReader(reader1);

                                    FileWriter writer = new FileWriter("Temp.txt",true);//make another file of Items.txt
                                    BufferedWriter bufferedWriter = new BufferedWriter(writer);//it help us to write the data in to file temp.txt

                                    String line1 = null;//it will read the line from the file
                                    String token1[] = {"","","","","","",""};//it split the line that read by the file
                                    //the following while loop help us to decrease the item quantity from file
                                    while ((line1 = bufferedReader1.readLine())!= null)
                                    {
                                        token1 = line1.split(",");
                                        if (itemNameField.getText().equals(token1[0]) && weightField.getText().equals(token1[3]))
                                        {
                                            int quantity1 = Integer.parseInt(itemQuantityField.getText());
                                            int quantityInfile = Integer.parseInt(token1[1]);
                                            bufferedWriter.write(token1[0]+","+(quantityInfile-quantity1) + ","+token1[2]+","+token1[3]
                                                    +","+token1[4]+","+token1[5]+","+token1[6]+","+token1[7]+"\n");


                                        }
                                        else
                                        {
                                            bufferedWriter.write(line1+"\n");
                                        }

                                    }


                                    bufferedReader.close();
                                    reader.close();

                                    bufferedWriter.close();
                                    writer.close();

                                    bufferedReader1.close();
                                    reader1.close();


//                                    File file = new File(FILENAME,"");
                                    Files.deleteIfExists(Paths.get(FILENAME));//it will remove the item.txt file
                                    File forRename = new File("Temp.txt");
                                    forRename.renameTo(new File(FILENAME));//it rename the file of temp.txt to item.txt
                                    flag = false;

                                    checker = false;

                                    itemNameField.clear();
                                    itemQuantityField.clear();
                                    weightField.clear();

                                }
                            }

                            if (checker){//here check that the item is add on table or not
                                try {//try catch for read line
                                    line = bufferedReader.readLine();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (flag)
                        {
                            Alert alert = new Alert(AlertType.ERROR,"I have not any product that use entered");
                            alert.showAndWait();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        });

        Label orLabel = new Label("OR");
        orLabel.setLayoutX(10);
        orLabel.setLayoutY(100);
        orLabel.setFont(new Font(18));

        Label serialLabel = new Label("Serial number : ");
        serialLabel.setLayoutY(150);
        serialLabel.setLayoutX(10);

        serialNumberField = new TextField();
        serialNumberField.setLayoutX(100);
        serialNumberField.setLayoutY(145);
        serialNumberField.setStyle("-fx-background-radius: 20px");

        Label serialNumberQuantityLabel = new Label("Quantity : ");
        serialNumberQuantityLabel.setLayoutX(270);
        serialNumberQuantityLabel.setLayoutY(150);

        serialNumberQuantity = new TextField();
        serialNumberQuantity.setLayoutX(330);
        serialNumberQuantity.setLayoutY(145);
        serialNumberQuantity.setPrefWidth(100);
        serialNumberQuantity.setStyle("-fx-background-radius: 20px");

        Button btnAddItemWithSerial = new Button("Add Item");
        btnAddItemWithSerial.setLayoutX(470);
        btnAddItemWithSerial.setLayoutY(145);
        btnAddItemWithSerial.setStyle("-fx-background-radius: 30px");
        //add action on this button
        btnAddItemWithSerial.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //when the fields is empty then the error message will appear
                if (serialNumberField.getText().isEmpty() || serialNumberQuantity.getText().isEmpty())
                {//this condition return true of the user click the button without any input
                    Alert alert = new Alert(AlertType.ERROR,"all fields are required");
                    alert.showAndWait();
                }
                else {
                    try {
                        //the following line is use to check the item from the file.
                        FileReader reader = null;
                        reader = new FileReader(FILENAME);//it read the data from file
                        BufferedReader bufferedReader = null;
                        bufferedReader = new BufferedReader(reader);//it help to read the data from file

                        String line = null;//it read a line from the file
                        String token[] = {};//it take the array of line from the file
                        boolean checker = true;//it check the user enter a valid serial number or not
                        boolean isPresent = true;//it check that the item added on table or not

                        FileWriter writer = null;// it is use to write the file
                        BufferedWriter bufferedWriter = null;//it help to write the data

                        writer = new FileWriter("Temp.txt", true);//write the data into another file witch is Items.txt
                        bufferedWriter = new BufferedWriter(writer);//it help us to write data into temp.txt file

//                        int quantity = 0;   //it tale the serial number a integer that the user enter
                        int quantityInFile = 1;//it take the data that the file have

                        while ((line = bufferedReader.readLine()) != null)//this loop will execute until the file is finis
                        {
                            token = line.split(",");//it split the line the take from the file


                            quantityInFile = Integer.parseInt(token[1]);
                            int quantity = Integer.parseInt(serialNumberQuantity.getText());

                            if (token[6].equals(serialNumberField.getText().trim()))//it check that the serialnumber is present in file or not
                            {
                                if (quantityInFile < quantity)//this condition check that the qunatity that we enter in the field is valid or not
                                {
                                    Alert alert = new Alert(AlertType.ERROR, "you enter invalid quantity which we have not ");//it show when the quantity is not valid
                                    alert.showAndWait();
                                } else {

                                    double price = Double.parseDouble(token[2]) * quantity;//it return the total price of the item

                                    double total = Double.parseDouble(totalPrice.getText());//it take the price that already store in a label
                                    total = total + price;//it add the total price with the price of label
                                    totalPrice.setText(String.valueOf(total));//it set the  new total value to the label
                                    for (int i = 0; i < table.getItems().size(); i++)//this loop will change the quantity of the item when it exist in table.
                                    {
                                        AddItmForClient addItmForClient = table.getItems().get(i);//it take the selected row item
                                        if (addItmForClient.getName().equals(token[0]))//this condition check that the value is present in table or not.
                                        {

                                            int quantity1 = Integer.parseInt(serialNumberQuantity.getText());//it take the quantity that user enter
                                            int quantityInTable = Integer.parseInt(addItmForClient.getItemQuantity());//it take the quantity that already store in table

                                            double priceInTable = Double.parseDouble(addItmForClient.getPrice());//it take the price that already store in table
                                            addItmForClient.setPrice("" + (price + priceInTable));//it add the the total value after add
                                            addItmForClient.setItemQuantity(String.valueOf((quantity1 + quantityInTable)));//it set new number of qquantity to table

                                            column1.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("name"));
                                            column2.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("itemQuantity"));
                                            column4.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("price"));
                                            column3.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("weight"));

                                            isPresent = false;//it tell us that the value is added to the table

                                        }
                                        if (!isPresent)//it check that value is present in table
                                            break;

                                    }
                                    if (isPresent)//it check that the value is not present in table right know
                                    {

                                        table.getItems().add(new AddItmForClient(token[0], serialNumberQuantity.getText(), "" + price, token[3], token[4], token[5], token[6], token[7]));
                                        column1.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("name"));
                                        column2.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("itemQuantity"));
                                        column4.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("price"));
                                        column3.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("weight"));


                                    }


                                    checker = false;//it will tell us that the item added on the table.

                                }
                            }

                            try {//in this try I remove the item fro the file when the item quantity will zero.


                                if (!(quantityInFile == 0)) {
                                    bufferedWriter.write(line + "\n");//if the qunatity of item is other then 0 then it write the line into temp.txt file
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isPresent = true;//again change the status of the isPresent to true


                        }

                        bufferedReader.close();//it close the file
                        reader.close();



                        bufferedWriter.close();
                        writer.close();

                        Files.deleteIfExists(Paths.get(FILENAME));

                        File forRename = new File("Temp.txt");
                        forRename.renameTo(new File(FILENAME));

                        if (checker)//it check that the product is not present or not.
                        {
                            Alert alert = new Alert(AlertType.INFORMATION, "We have not any product regarding this serial number");
                            alert.showAndWait();
                        }


                        checker = true;

                        //decrease the quantity from the file

                        FileReader fileReader = new FileReader(FILENAME);
                        BufferedReader bufferedReader1 = new BufferedReader(fileReader);

                        FileWriter fileWriter = new FileWriter("Temp.txt", true);
                        BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter);

                        while ((line = bufferedReader1.readLine()) != null) {
                            token = line.split(",");
                            System.out.println("token[6] : " + token[6]);
                            System.out.println("serial number : "+serialNumberField.getText());
                            if (serialNumberField.getText().trim().equals(token[6])) {
                                System.out.println("in function");
                                int quantity = Integer.parseInt(serialNumberQuantity.getText().trim());
                                quantityInFile = Integer.parseInt(token[1]) - quantity;
                                bufferedWriter1.write(token[0] + "," + quantityInFile + "," + token[2] + "," + token[3] + "," + token[4]
                                        + "," + token[5] + "," + token[6] + "," + token[7]+"\n");

                            } else {
                                bufferedWriter1.write(line + "\n");
                                System.out.println("in else");
                            }
                        }

                        serialNumberQuantity.clear();//it clear the field
                        serialNumberField.clear();//it also clear the field


                        bufferedReader1.close();
                        fileReader.close();

                        bufferedWriter1.close();
                        fileWriter.close();

                        Files.deleteIfExists(Paths.get(FILENAME));
                        File forRename1 = new File("Temp.txt");
                        forRename1.renameTo(new File(FILENAME));


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }





                }
            }
        });

        totalPrice = new Label("0.0");
        totalPrice.setLayoutY(320);
        totalPrice.setLayoutX(750);
        totalPrice.setFont(new Font(20));

        Label totalPriceLabel = new Label("Total price : ");
        totalPriceLabel.setLayoutY(320);
        totalPriceLabel.setLayoutX(630);
        totalPriceLabel.setFont(new Font(20));

        Label castomer = new Label("Castomer Enter : ");
        castomer.setLayoutX(630);
        castomer.setLayoutY(370);

        castomerBalance = new TextField();
        castomerBalance.setLayoutX(750);
        castomerBalance.setLayoutY(370);
        castomerBalance.setPrefWidth(100);
        castomerBalance.setStyle("-fx-background-radius: 20px");



        Label line = new Label("_________________________________________________");
        line.setLayoutY(400);
        line.setLayoutX(630);

        Label remainingBalance = new Label("Remaining : ");
        remainingBalance.setLayoutX(630);
        remainingBalance.setLayoutY(420);

        remaining = new Label("0.0");
        remaining.setLayoutX(750);
        remaining.setLayoutY(420);

        Button btnRemaining = new Button("Remaining");
        btnRemaining.setLayoutX(650);
        btnRemaining.setLayoutY(450);
        btnRemaining.setStyle("-fx-background-radius: 20px");
        btnRemaining.setPrefWidth(100);
        btnRemaining.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (totalPrice.getText().equals("0.0"))
                {
                    Alert alert = new Alert(AlertType.WARNING,"You have not enter any Item so" +
                            "\n we can't make the recipt");
                    alert.showAndWait();
                }
                else if (castomerBalance.getText().isEmpty())
                {
                    Alert alert = new Alert(AlertType.WARNING,"you have not enter any amount from user/client");
                    alert.showAndWait();
                }
                else
                {
                    double totalaAmount = Double.parseDouble(totalPrice.getText());
                    double userAmount = Double.parseDouble(castomerBalance.getText());
                    if (totalaAmount > userAmount)
                    {
                        Alert alert = new Alert(AlertType.WARNING,"you enter the clent price less then required");
                        alert.showAndWait();
                    }
                    else {

                        remaining.setText(String.valueOf((userAmount - totalaAmount)));
                        try {
                            FileReader reader = new FileReader(FILETHATSTORECLIENTID);//it helps us to find the last client id
                            BufferedReader bufferedReader = new BufferedReader(reader);

                            int id = Integer.parseInt(bufferedReader.readLine());//it take the last client id
                            id += 1;//increment in id

                            bufferedReader.close();//close the file
                            reader.close();//close the file

                            FileWriter writer = new FileWriter(TEMP,true);
                            BufferedWriter bufferedWriter = new BufferedWriter(writer);

                            bufferedWriter.write(String.valueOf(id) + "\n");

                            bufferedWriter.close();
                            writer.close();

                            Files.deleteIfExists(Paths.get(FILETHATSTORECLIENTID));
                           File forRename = new File(TEMP);
                           forRename.renameTo(new File(FILETHATSTORECLIENTID));


                           FileWriter writer1 = new FileWriter(FILETHATSTOREHISTORYOFCLIENT,true);
                           BufferedWriter bufferedWriter1 = new BufferedWriter(writer1);
                           String storeHistory = String.valueOf(id);
                           Date date = new Date();
                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy,hh:mm:ss");
                           storeHistory = storeHistory+"," + dateFormat.format(date);
                           //the history will store in this formate
                            //client id , total price , date , time , item that purchase , item quantity , item price , ....
                           for (int i = 0;i<table.getItems().size();i++)
                           {
                               AddItmForClient addItmForClient = table.getItems().get(i);
                               storeHistory = storeHistory + ","+addItmForClient.getSerial()+","+addItmForClient.getItemQuantity()+","+addItmForClient.getPrice();
                           }
                           storeHistory = storeHistory + "\n";
                           bufferedWriter1.write(storeHistory);

                           bufferedWriter1.close();
                           writer1.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Button btnPrint = new Button("Generate Racipt");
        btnPrint.setLayoutY(450);
        btnPrint.setLayoutX(760);
        btnPrint.setStyle("-fx-background-radius: 20px");

        btnPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String reciptString = "Utility Store\n--------------------------------------\n\n";
                reciptString = reciptString + "itemName   item Qty   Price\n--------------------------------------\n";
                for (int i = 0;i<table.getItems().size();i++)
                {
                    AddItmForClient addItmForClient = table.getItems().get(i);
                    int size = addItmForClient.getName().length();
                    reciptString = reciptString + addItmForClient.getName();
                    for (int space = size;space <= 16;space++)
                        reciptString += " ";
                    reciptString += addItmForClient.getItemQuantity();
                    size = addItmForClient.getItemQuantity().length();
                    for (int space = size;space<=22;space++)
                        reciptString += " ";
                    reciptString += addItmForClient.getPrice()+"\n";
                }
                reciptString = reciptString + "\n--------------------------------------\n";
                reciptString = reciptString + "\t\tTotal Amount : \t"+totalPrice.getText()+"\n";
                reciptString = reciptString + "\t\tUser Balance Enter :\t"+castomerBalance.getText()+"\n";
                reciptString = reciptString + "--------------------------------------\n";
                reciptString = reciptString + "\t\t\tRemaining : \t"+remaining.getText()+"\n";

                TextArea textArea = new TextArea(reciptString);


                Printer printers = Printer.getDefaultPrinter();
                if (printers != null)
                {
                    textArea.appendText(printers.getName()+"\n");
                    PrinterJob job = PrinterJob.createPrinterJob();
                    textArea.textProperty().bind(job.jobStatusProperty().asString());

                    if (job.printPage(textArea))
                    {
                        job.endJob();
                    }


                }
                else
                {
                    Alert alert = new Alert(AlertType.WARNING,"you have no any print to print the recipt");
                    alert.showAndWait();
                }

            }
        });

        anchorPane.getChildren().addAll(itemNameLabel,itemNameField);
        anchorPane.getChildren().addAll(itemQuantityLabel,itemQuantityField);
        anchorPane.getChildren().addAll(serialLabel,serialNumberField);
        anchorPane.getChildren().addAll(weightLabel,weightField,orLabel);
        anchorPane.getChildren().addAll(serialNumberQuantityLabel,serialNumberQuantity);
        anchorPane.getChildren().addAll(btnAddItemWithName,btnAddItemWithSerial);
        anchorPane.getChildren().addAll(totalPrice,totalPriceLabel);
        anchorPane.getChildren().addAll(castomer,castomerBalance);
        anchorPane.getChildren().addAll(remainingBalance,remaining,line);
        anchorPane.getChildren().addAll(btnRemaining,btnPrint);
    }
    public void makeMenus()
    {
        menuBar = new MenuBar();

        //make menu for file
        Menu fileMenu = new Menu("File");

        MenuItem addStoke = new MenuItem("Add Stoke...");
        addStoke.setAccelerator(new KeyCodeCombination(KeyCode.A,KeyCombination.CONTROL_DOWN));
        addStoke.setOnAction(e->new Stock().start(new Stage()));

        MenuItem newWindow = new MenuItem("New Window...");
        newWindow.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));
        newWindow.setOnAction(e-> {
            try {
                new Main().start(new Stage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        closeMenu = new MenuItem("Close");
        closeMenu.setAccelerator(new KeyCodeCombination(KeyCode.E,KeyCombination.CONTROL_DOWN));


        //add menuitem in fileManu
        fileMenu.getItems().add(addStoke);
        fileMenu.getItems().add(newWindow);
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(closeMenu);

        //make menu for edit
        Menu editMenu = new Menu("_Edit");
        MenuItem findMenuItem = new MenuItem("Find");
        //add shortcut key
        findMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F,KeyCombination.CONTROL_DOWN));
        //make action on find menuitem
        findMenuItem.setOnAction(e-> {
            try {

                int sizeOfTeble = table.getItems().size();

                Label findLabel = new Label("Enter Item to find");
                findLabel.setLayoutY(10);
                findLabel.setLayoutX(800);

                TextField findField = new TextField();
                findField.setLayoutX(800);
                findField.setLayoutY(30);
                findField.setStyle("-fx-background-radius: 20px");


                Button btnFind = new Button("Find");
                btnFind.setLayoutX(850);
                btnFind.setLayoutY(60);
                btnFind.setStyle("-fx-background-radius: 20px");
                btnFind.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (findField.getText().isEmpty())
                        {
                            Alert alert = new Alert(AlertType.INFORMATION,"field is required to find Items from Table");
                            alert.showAndWait();
                        }
                        else if (table.getItems().isEmpty())
                        {
                            Alert alert = new Alert(AlertType.INFORMATION,"table is empty so we can't find any thing");
                            alert.showAndWait();
                        }
                        else
                        {
                            for (int i = 0; i < sizeOfTeble; i++) {
                                AddItmForClient temp = table.getItems().get(i);
                                if (findField.getText().equals(temp.getName()) || findField.getText().equals(temp.getItemQuantity()) ||
                                        findField.getText().equals(temp.getPrice()) || findField.getText().equals(temp.getWeight())
                                        || findField.getText().equals(temp.getSerial())) {

                                    table.getItems().remove(i);
                                    table.getItems().add(0, temp);

                                }
                            }

                            findField.clear();
                        }
                    }
                });


                Button btnClose = new Button("X");
                btnClose.setLayoutX(950);
                btnClose.setLayoutY(5);
                btnClose.setStyle("-fx-background-radius: 20px");
                btnClose.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        anchorPane.getChildren().remove(btnFind);
                        anchorPane.getChildren().remove(findField);
                        anchorPane.getChildren().remove(findLabel);
                        anchorPane.getChildren().remove(btnClose);


                    }
                });


                anchorPane.getChildren().addAll(findField,btnClose,btnFind);
                anchorPane.getChildren().add(findLabel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        //add menu item to edit menu
        
        editMenu.getItems().add(findMenuItem);

        //make view  menu
        Menu viewMenu = new Menu("_View");
        MenuItem viewHistory = new MenuItem("View history");
        viewMenu.getItems().addAll(viewHistory);
        viewHistory.setAccelerator(new KeyCodeCombination(KeyCode.H,KeyCombination.CONTROL_DOWN));
        viewHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new ShowHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //make help menu
        Menu helpMenu = new Menu("Help");
        MenuItem help = new MenuItem("Help");
        help.setAccelerator(new KeyCodeCombination(KeyCode.H,KeyCombination.CONTROL_DOWN));
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION,"Dear User!\n" +
                        "\t\tIf you need some help to use this software you may contact our" +
                        " team to remove the problem");
                alert.showAndWait();

            }
        });
        MenuItem about = new MenuItem("About");
        about.setAccelerator(new KeyCodeCombination(KeyCode.A,KeyCombination.CONTROL_DOWN));
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION,"\n" +
                        "Build #IU-201.6668.121, built on April 8, 2020\n" +
                        "Licensed to https://zhile.io\n" +
                        "You have a perpetual fallback license for this version\n" +
                        "Subscription is active until July 8, 2089\n" +
                        "Runtime version: 11.0.6+8-b765.25 amd64\n" +
                        "Windows 10 10.0\n" +
                        "GC: ParNew, ConcurrentMarkSweep\n" +
                        "Memory: 976M\n" +
                        "Cores: 4\n" +
                        "Non-Bundled Plugins: mobi.hsz.idea.latex");
                alert.setHeaderText("Utility Store");
                alert.showAndWait();
            }
        });
        MenuItem update = new MenuItem("Update");
        update.setAccelerator(new KeyCodeCombination(KeyCode.U,KeyCombination.CONTROL_DOWN));
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION,"you run the latest version of the spftware\n" +
                        "if you want to add some new functionalities in you software\n" +
                        "the contact to the developer team  of you software");
                alert.showAndWait();
            }
        });
        helpMenu.getItems().addAll(help,about,update);

        menuBar.getMenus().addAll(fileMenu,editMenu,viewMenu,helpMenu);


    }

    public void makeTable()
    {
        table = new TableView<>();
        column1 = new TableColumn("Item Name");
        column1.setPrefWidth(150);
        column2 = new TableColumn("Item Quantity");
        column2.setPrefWidth(150);
        column3 = new TableColumn("Item weight");
        column3.setPrefWidth(150);
        column4 = new TableColumn("Price");
        column4.setPrefWidth(150);


        table.setLayoutX(20);
        table.setLayoutY(250);
        table.setPrefWidth(600);
        table.setPrefHeight(300);
        table.setStyle("-fx-background-radius: 50px");

       btnDeleteRow = new Button("Remove");
        btnDeleteRow.setLayoutX(20);
        btnDeleteRow.setLayoutY(560);
        btnDeleteRow.setPrefWidth(70);
        btnDeleteRow.setStyle("-fx-background-radius: 40px");
        btnDeleteRow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (table.getItems().isEmpty())
                {
                    Alert alert = new Alert(AlertType.WARNING,"The table is empty");
                    alert.setHeaderText("we can't do that");
                    alert.showAndWait();
                }
                else if (table.getSelectionModel().getSelectedIndex() == -1)
                {
                    Alert alert = new Alert(AlertType.WARNING,"you must select the row to remove");
                    alert.setHeaderText("we can't do that");
                    alert.showAndWait();
                }
                else {
                    AddItmForClient addItmForClient = table.getSelectionModel().getSelectedItem();
                    table.getItems().remove(table.getSelectionModel().getSelectedIndex());
                    try {
                        FileWriter fileWriter = new FileWriter("Temp.txt",true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        FileReader reader = new FileReader(FILENAME);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line = null;
                        String token[] = {};
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            token = line.split(",");
                            if (token[0].equals(addItmForClient.getName()) && token[3].equals(addItmForClient.getWeight()))
                            {
                                int quantity = Integer.parseInt(token[1]);
                                int quantityInTable = Integer.parseInt(addItmForClient.getItemQuantity());
                                quantity += quantityInTable;
                                token[1] = ""+quantity;
                                bufferedWriter.write(token[0]+","+token[1]+","+token[2]+","+token[3]+","+token[4]+","+token[5]+","+token[6]+","+token[7]+"\n");
                                double price = Double.parseDouble(addItmForClient.getPrice());
                                double tPrice = Double.parseDouble(totalPrice.getText()) - price;
                                totalPrice.setText(""+tPrice);
                            }
                            else if (!(token[1].equals("0")))
                                bufferedWriter.write(line+"\n");
                        }

                        bufferedReader.close();
                        reader.close();
                        bufferedWriter.close();
                        fileWriter.close();

                        Files.deleteIfExists(Paths.get(FILENAME));

                        File forRename = new File("Temp.txt");
                        forRename.renameTo(new File(FILENAME));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        anchorPane.getChildren().add(btnDeleteRow);

        table.getColumns().addAll(column1,column2,column3,column4);


        anchorPane.getChildren().addAll(table);


    }

    public void enterItem(AddItmForClient addItmForClient)
    {
        ObservableList<AddItmForClient> data =  FXCollections.observableArrayList(
            addItmForClient );
        table.getItems().add(addItmForClient);

        column1.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("name"));
        column2.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("itemQuantity"));
        column4.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("price"));
        column3.setCellValueFactory(new PropertyValueFactory<AddItmForClient, SimpleStringProperty>("weight"));

    }
    public static void main(String[] args) {

        try {
            FileWriter writer = new FileWriter("D:\\Items.txt",true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            if(!Files.exists(Paths.get("D:\\ID.txt")))
            {
                FileWriter fileWriter = new FileWriter("D:\\ID.txt",true);
                BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter);
                bufferedWriter1.write("0\n");
                bufferedWriter1.close();
                fileWriter.close();
            }

            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch();
    }
}
