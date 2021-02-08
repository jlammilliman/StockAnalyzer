package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * INTERFACE: StockAnalyzerInterface
 * DESCRIPTION: a set of methods used to analyze stock data.
 * @author Leo Ureel
 */
public interface StockAnalyzerInterface {

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
	 * @param file
	 * @return The list of stocks read in from the specified file.
	 * @throws FileNotFoundException
	 */
	public abstract ArrayList<AbstractStock> loadStockData(File file) throws FileNotFoundException;

	/**
	 * @return a list of all stocks that have been loaded
	 */
	public abstract ArrayList<AbstractStock> listStocks();

	/**
	 * @param symbol
	 * @return a list of all stocks with the specified ticker symbol.
	 */
	public abstract ArrayList<AbstractStock> listBySymbol(String symbol);

	/**
	 * @param symbol
	 * @param startDate
	 * @param endDate
	 * @return a list of stocks with the specified symbol recorded between
	 *         (and including) the start and end dates.
	 */
	public abstract ArrayList<AbstractStock> listBySymbolDates(String symbol, LocalDate startDate, LocalDate endDate);

	/**
	 * @param symbol
	 * @param startDate
	 * @param endDate
	 * @return the average high value of all stocks with the specified symbol
	 *         recorded between (and including) the start and end dates.
	 * @throws StockNotFoundException
	 */
	public abstract double averageHigh(String symbol, LocalDate startDate, LocalDate endDate) throws StockNotFoundException;

	/**
	 * @param symbol
	 * @param startDate
	 * @param endDate
	 * @return the average low value of all stocks with the specified symbol
	 *         recorded between (and including) the start and end dates.
	 * @throws StockNotFoundException
	 */
	public abstract double averageLow(String symbol, LocalDate startDate, LocalDate endDate) throws StockNotFoundException;

	/**
	 * @param symbol
	 * @param startDate
	 * @param endDate
	 * @return the average volume value of all stocks with the specified symbol
	 *         recorded between (and including) the start and end dates.
	 * @throws StockNotFoundException
	 */
	public abstract double averageVolume(String symbol, LocalDate startDate, LocalDate endDate) throws StockNotFoundException;

}
