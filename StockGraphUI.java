package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**@author : Justin Milliman
 * CS1122 - R02 - Introduction to Programming II
 * Date Last Modified : 10/24/2020
 * Assignment : StockGraph User Interface Program
 * ==================================================================
 * In this program we take stock data from .csv files and load them into a
 * stockAnalyzer to be displayed in a javaFX lineChart.
 */
public class StockGraphUI extends Application {

    //===============================================================
    // start/main
    //===============================================================

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) throws Exception{
        Pane root = createRootPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    //===============================================================
    // Helpful UI creation methods
    //===============================================================

    /** createRootPane : a helpful method that defines the layout of the UI
     *
     * @return : returns the pane with all the added elements we want in the UI
     */
    public Pane createRootPane(){
        BorderPane root = new BorderPane();
        root.setMinSize(800,500);

        // Make base lineChart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setStyle("line");
        root.setCenter(lineChart);

        // Get command Arguments
        Parameters params = getParameters();
        List<String> parameters = params.getRaw();


        //===============================================================
        // Create CheckBoxes
        //===============================================================

        Label openLabel = new Label("Open");
        CheckBox checkOpen = new CheckBox();
        Label highLabel = new Label("High");
        CheckBox checkHigh = new CheckBox();
        Label lowLabel = new Label("Low");
        CheckBox checkLow  = new CheckBox();
        Label closeLabel = new Label("Close");
        CheckBox checkClose= new CheckBox();


        // ===============================================================
        // Create ComboBox
        // ===============================================================

        Label comboLabel = new Label("Select Stock: ");
        ComboBox<String> stockSelectComboBox = new ComboBox<>();
        for (String s : parameters) {
            String symbol = s.substring(0, s.length() - 4);//remove .csv
            stockSelectComboBox.getItems().add(symbol);
        }
        stockSelectComboBox.getSelectionModel().select(0);
        stockSelectComboBox.setOnAction(event -> {

            //Create a new stockAnalyzer each time, else it breaks for some reason..
            StockAnalyzer stockAnalyzer = new StockAnalyzer();
            try {
                for (String s : parameters) {
                    System.out.println("Successfully Loaded File : " + s);
                    File file = new File(s);
                    stockAnalyzer.loadStockData(file);
                }
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }

            //Remove all children
            for(int i = lineChart.getData().size() - 1; i >= 0; i--){
                lineChart.getData().remove(i);
            }

            //Get current selected symbol and make the lineChart
            String stockSymbol = stockSelectComboBox.getSelectionModel().getSelectedItem();
            lineChart.setTitle("Stock Monitoring: " + stockSymbol);

            //Check checkBoxes and make appropriate series
            //Open
            if(checkOpen.isSelected()){
                final XYChart.Series sOpen = graphOpen(stockAnalyzer, stockSymbol);
                lineChart.getData().add(sOpen);
            }

            //High
            if(checkHigh.isSelected()){
                final XYChart.Series sHigh = graphHigh(stockAnalyzer, stockSymbol);
                lineChart.getData().add(sHigh);
            }

            //Low
            if(checkLow.isSelected()){
                final XYChart.Series sLow = graphLow(stockAnalyzer, stockSymbol);
                lineChart.getData().add(sLow);
            }

            //Close
            if(checkClose.isSelected()){
                final XYChart.Series sClose = graphClose(stockAnalyzer, stockSymbol);
                lineChart.getData().add(sClose);
            }

            root.setCenter(lineChart);
        });



        // ===============================================================
        // Create SelectionBar
        // ===============================================================

        HBox selectionBar = new HBox(
                comboLabel, stockSelectComboBox,
                openLabel,  checkOpen,
                highLabel,  checkHigh,
                lowLabel,   checkLow,
                closeLabel, checkClose
        );


        //===============================================================
        // Create StatusBar
        //===============================================================

        HBox statusBarPane = new HBox();
        statusBarPane.setPadding(new Insets(5, 4, 5, 4));
        statusBarPane.setSpacing(10);
        statusBarPane.setStyle("-fx-background-color: #336699;");
        TextField statusBar = new TextField();
        HBox.setHgrow(statusBar, Priority.ALWAYS);

        //Get Location in lineChart
        lineChart.setOnMousePressed (( MouseEvent event) -> {
            Point2D mouseSceneCoords = new Point2D( event.getSceneX ( ), event.getSceneY ( ));
            String x = xAxis.getValueForDisplay ( xAxis.sceneToLocal(mouseSceneCoords).getX() );
            Number y = yAxis.getValueForDisplay ( yAxis.sceneToLocal(mouseSceneCoords).getY() );
            statusBar.setText("(" + x + ", " + y + ")");
                });

        statusBarPane.getChildren().addAll(statusBar);


        //===============================================================
        // Finalize root
        //===============================================================

        root.setTop(selectionBar);
        root.setBottom(statusBarPane);
        return root;
    }


    //===============================================================
    // Series makers, more convenient
    //===============================================================

    /** graphOpen : creates a series of all the open data from a desired stock type
     *
     * @param stockAnalyzer : takes in the loaded stock data
     * @param symbol : takes the four letter symbol for the desired stock data you wish to graph
     * @return : returns the series of the desired open data obtained from the stockAnalyzer
     */
    public XYChart.Series graphOpen(StockAnalyzer stockAnalyzer, String symbol){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        XYChart.Series sOpen = new XYChart.Series();
        sOpen.setName("Open");

        ArrayList<AbstractStock> stocks = stockAnalyzer.listBySymbol(symbol);
        for (AbstractStock s : stocks) {
            if (s.getSymbol().compareTo(symbol) == 0) {

                // Convert timestamp to date
                Instant instant = Instant.ofEpochMilli(s.getTimestamp());
                LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                String fd = formatter.format(date);
                System.out.println(fd);

                sOpen.getData().add(new XYChart.Data(fd, s.getOpen()));
            }
        }
        return sOpen;
    }

    /** graphHigh : creates a series of all the high data from a desired stock type
     *
     * @param stockAnalyzer : takes in the loaded stock data
     * @param symbol : takes the four letter symbol for the desired stock data you wish to graph
     * @return : returns the series of the desired high data obtained from the stockAnalyzer
     */
    public XYChart.Series graphHigh(StockAnalyzer stockAnalyzer, String symbol){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        XYChart.Series sHigh = new XYChart.Series();
        sHigh.setName("High");

        ArrayList<AbstractStock> stocks = stockAnalyzer.listBySymbol(symbol);
        for (AbstractStock s : stocks) {
            if (s.getSymbol().compareTo(symbol) == 0) {
                Instant instant = Instant.ofEpochMilli(s.getTimestamp());
                LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                String fd = formatter.format(date);
                System.out.println(fd);

                sHigh.getData().add(new XYChart.Data(fd, s.getHigh()));
            }
        }
        return sHigh;
    }


    /** graphLow : creates a series of all the low data from a desired stock type
     *
     * @param stockAnalyzer : takes in the loaded stock data
     * @param symbol : takes the four letter symbol for the desired stock data you wish to graph
     * @return : returns the series of the desired low data obtained from the stockAnalyzer
     */
    public XYChart.Series graphLow(StockAnalyzer stockAnalyzer, String symbol){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        XYChart.Series sLow = new XYChart.Series();
        sLow.setName("Low");

        ArrayList<AbstractStock> stocks = stockAnalyzer.listBySymbol(symbol);
        for (AbstractStock s : stocks) {
            if (s.getSymbol().compareTo(symbol) == 0) {
                Instant instant = Instant.ofEpochMilli(s.getTimestamp());
                LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                String fd = formatter.format(date);
                System.out.println(fd);

                sLow.getData().add(new XYChart.Data(fd, s.getLow()));
            }
        }
        return sLow;
    }


    /** graphClose : creates a series of all the close data from a desired stock type
     *
     * @param stockAnalyzer : takes in the loaded stock data
     * @param symbol : takes the four letter symbol for the desired stock data you wish to graph
     * @return : returns the series of the desired close data obtained from the stockAnalyzer
     */
    public XYChart.Series graphClose(StockAnalyzer stockAnalyzer, String symbol){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        XYChart.Series sClose = new XYChart.Series();
        sClose.setName("Close");

        ArrayList<AbstractStock> stocks = stockAnalyzer.listBySymbol(symbol);
        for (AbstractStock s : stocks) {
            if (s.getSymbol().compareTo(symbol) == 0) {
                Instant instant = Instant.ofEpochMilli(s.getTimestamp());
                LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                String fd = formatter.format(date);
                System.out.println(fd);

                sClose.getData().add(new XYChart.Data(fd, s.getClose()));
            }
        }
        return sClose;
    }
}