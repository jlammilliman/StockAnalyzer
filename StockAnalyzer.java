package sample;

import com.sun.javafx.scene.traversal.TopMostTraversalEngine;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StockAnalyzer implements StockAnalyzerInterface {
    private ArrayList<AbstractStock> stockFileData = new ArrayList<>();

    ZoneId systemZone = ZoneId.systemDefault(); // my timezone
    ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(Instant.now());

    /**
     * Loads stock data from the specified file.
     * Merges the data with previously loaded data.
     *
     * Data files are formatted in a Comma Separated Values (CSV) text file format
     * Each line contains the following values:
     * Date,Open,High,Low,Close,Adj Close,Volume
     *
     * The first line of the file contains the column headings.
     *
     * NOTES:
     * 1. Ignore the Adj Close data.
     * 2. You must convert the Date to a timestamp.
     * 3. The stock ticker symbol is the file name minus the file extension.
     * 4. Skip (Reject) any stock whose data is invalid.
     *
     * @param file insert the name of the file you want to pull stock data from
     * @return The list of stocks read in from the specified file.
     * @throws FileNotFoundException
     */
    @Override
    public ArrayList<AbstractStock> loadStockData (File file ) throws FileNotFoundException{
        Scanner scanStockFile = new Scanner(file);
        scanStockFile.useDelimiter(",|\n");//Set it up to read commas as break points

        //For debugging
        //System.out.println("Successfully loaded: " + file);

        //Create basic stock object and ArrayList to house stock objects
        Stock stock;

        //Grab Symbol from File name
        String s = file.getName();
        String symbol = s.substring(0, s.length() - 4); // 4 = length of .csv

        scanStockFile.nextLine();//Skip the first line that gives column names

        //Loop through each line of the file, create a stock object of that days info, and add to array
        while(scanStockFile.hasNextLine() & scanStockFile.hasNext()){//This looks nastier than it is
            //Convert date YYYY-MM-DD to a long
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse ( scanStockFile.next(), formatter );
            long timestamp = date.atTime ( 16, 0, 0 ).toInstant(currentOffsetForMyZone).toEpochMilli ( );

            double open, high, low, close, volume;

            //Have to add in some null protection otherwise everything explodes

            boolean makeStock = true;

            //Open
            String str = scanStockFile.next();
            if (str.compareTo("null") != 0) {
                open = Double.parseDouble(str);
            }else{
                open = 0.0;
                makeStock = false;
            }

            //High
            str = scanStockFile.next();
            if (str.compareTo("null") != 0) {
                high = Double.parseDouble(str);
            }else{
                high = 0.0;
                makeStock = false;
            }

            //Low
            str = scanStockFile.next();
            if (str.compareTo("null") != 0) {
                low = Double.parseDouble(str);
            }else{
                low = 0.0;
                makeStock = false;
            }

            //Close
            str = scanStockFile.next();
            if (str.compareTo("null")!=0) {
                close = Double.parseDouble(str);
            }else{
                close = 0.0;
                makeStock = false;
            }

            scanStockFile.next();//Skip adj Close

            //Volume
            str = scanStockFile.next();
            if (str.compareTo("null")!=0) {
                volume = Double.parseDouble(str);
            }else{
                volume = 0.0;
                makeStock = false;
            }

            //Create stock object and dump to array
            if(makeStock){
                stock = new Stock(symbol,timestamp,open,high,low,close,volume);
                stockFileData.add(stock);
            }
        }
        scanStockFile.close();
        return stockFileData;//Not necessary as a return type, but here for convenience or something
    };

    /**
     * @return a list of all stocks that have been loaded
     */
    @Override
    public ArrayList<AbstractStock> listStocks( ){
        return stockFileData;
    }

    /**
     * @param symbol
     * @return a list of all stocks with the specified ticker symbol.
     */
    @Override
    public ArrayList<AbstractStock> listBySymbol( String symbol ){
        ArrayList<AbstractStock> specificStockData = new ArrayList<>();
        int count = 0;
        //Loop through and check to see if the symbol matches, if it does add it to the return array
        for(AbstractStock stock : stockFileData){
            if(symbol.compareTo(stock.getSymbol()) == 0){
                specificStockData.add(stock);
                count ++;
            }
        }
        stockFileData = specificStockData;
        return stockFileData;
    }

    /**
     * @param symbol
     * @param startDate
     * @param endDate
     * @return a list of stocks with the specified symbol recorded between
     *         (and including) the start and end dates.
     */
    public ArrayList<AbstractStock> listBySymbolDates ( String symbol, LocalDate startDate, LocalDate endDate ){
        ArrayList<AbstractStock> returnThisStockData = new ArrayList<>();
        Instant instant;
        LocalDate date;

        for(int i = 0; i < listBySymbol(symbol).size(); i++){
            //Convert the timestamp back into a LocalDate object
            instant = Instant.ofEpochMilli(listBySymbol(symbol).get(i).getTimestamp());
            date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            //Check to make sure the stock object is within the desired dates before adding to the return array
            if(startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0){
                returnThisStockData.add(listBySymbol(symbol).get(i));
            }
        }
        this.stockFileData = returnThisStockData;
        return stockFileData;
    }

    /**
     * @param symbol
     * @param startDate
     * @param endDate
     * @return the average high value of all stocks with the specified symbol
     *         recorded between (and including) the start and end dates.
     * @throws StockNotFoundException
     */
    @Override
    public double averageHigh(String symbol, LocalDate startDate, LocalDate endDate ) throws StockNotFoundException{
        Instant instant;
        LocalDate date;
        double average = 0.0;
        double n = 0.0;
        for(AbstractStock stock : stockFileData){
            instant = Instant.ofEpochMilli(stock.getTimestamp());
            date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            if(symbol.compareTo(stock.getSymbol()) == 0 && startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
                average += stock.getHigh();
                n++;
            }
        }

        if(average/n > 0){
            return average/n;
        }else{
            throw new StockNotFoundException("Woopsie Daisy");
        }
    }

    /**
     * @param symbol
     * @param startDate
     * @param endDate
     * @return the average low value of all stocks with the specified symbol
     *         recorded between (and including) the start and end dates.
     * @throws StockNotFoundException
     */
    @Override
    public double averageLow( String symbol, LocalDate startDate, LocalDate endDate ) throws StockNotFoundException{
        Instant instant;
        LocalDate date;
        double average = 0;
        double n = 0;
        for(AbstractStock stock : stockFileData){
            instant = Instant.ofEpochMilli(stock.getTimestamp());
            date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            if(symbol.compareTo(stock.getSymbol()) == 0 && startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
                average += stock.getLow();
                n++;
            }
        }
        if(average/n > 0){
            return average/n;
        }else{
            throw new StockNotFoundException("Woopsie Daisy");
        }
    }

    /**
     * @param symbol
     * @param startDate
     * @param endDate
     * @return the average volume value of all stocks with the specified symbol
     *         recorded between (and including) the start and end dates.
     * @throws StockNotFoundException
     */
    @Override
    public double averageVolume( String symbol, LocalDate startDate, LocalDate endDate ) throws StockNotFoundException{
        Instant instant;
        LocalDate date;
        double volume = 0;
        double n = 0;

        for(AbstractStock stock : stockFileData){
            instant = Instant.ofEpochMilli(stock.getTimestamp());
            date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            if(symbol.compareTo(stock.getSymbol()) == 0 && startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
                volume += stock.getVolume();
                n++;
            }
        }
        if(volume/n > 0){
            return volume/n;
        }else{
            throw new StockNotFoundException("Woopsie Daisy");
        }
    }
}