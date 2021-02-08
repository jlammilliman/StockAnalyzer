package sample;

import java.io.File;
import java.util.Date;

/**
 * CLASS: AbstractStock
 * DESCRIPTION Encapsulates information about a stock on the date indicated by
 *              the timestamp. The information encapsulated is:
 *              symbol: the ticker symbol of the stock (e.g. MSFT for Microsoft)
 *              timestamp: 11:59pm on the trading day when the data was obtained
 *              open: the opening price of the stock
 *              high: the highest price at which the stock traded that day
 *              low: the lowest price at which the stock traded that day
 *              close: the price at the end of trading for the day
 *              volume: the number of shares traded that day
 * NOTES:
 * 1. A timestamp is the number of milliseconds since January 1, 1970,
 *    00:00:00 GMT up to a specific date. You should assume the time for all
 *    dates is 11:59pm. This will encompass a whole day of trading.
 *
 * @author Leo Ureel
 */
public abstract class AbstractStock {
	private String symbol = null;
	private Long timestamp = null;
	private double open = 0.0;
	private double high = 0.0;
	private double low = 0.0;
	private double close = 0.0;
	private double volume = 0.0;

	/**
	 * Constructor used to initialize a new instance of the stock
	 * @param symbol : the ticker symbol of the stock (e.g. MSFT for Microsoft)
	 * @param timestamp : 11:59pm on the trading day when the data was obtained
	 * @param open : the opening price of the stock
	 * @param high : the highest price at which the stock traded that day
	 * @param low : the lowest price at which the stock traded that day
	 * @param close : the price at the end of trading for the day
	 * @param volume : the number of shares traded that day
	 */
	public AbstractStock ( String symbol, Long timestamp, double open,
	                       double high, double low, double close,
	                       double volume ) {
		this.symbol = symbol;
		this.timestamp = timestamp;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	/**
	 * @return the ticker symbol of the stock (e.g. "MSFT" for Microsoft)
	 */
	public String getSymbol ( ) {
		return symbol;
	}

	/**
	 * @return 11:59pm on the trading day when the data was obtained
	 * NOTE: A timestamp is the number of milliseconds since January 1, 1970,
	 *       00:00:00 GMT up to a specific date. You should assume the time for all
	 *       dates is 11:59pm. This will encompass a whole day of trading.
	 */
	public Long getTimestamp ( ) {
		return timestamp;
	}

	/**
	 * @return the opening price of the stock
	 */
	public double getOpen ( ) {
		return open;
	}

	/**
	 * @return the highest price at which the stock traded that day
	 */
	public double getHigh ( ) {
		return high;
	}

	/**
	 * @return the lowest price at which the stock traded that day
	 */
	public double getLow ( ) {
		return low;
	}

	/**
	 * @return the price at the end of trading for the day
	 */
	public double getClose ( ) {
		return close;
	}

	/**
	 * @return the number of shares traded that day
	 */
	public double getVolume ( ) {
		return volume;
	}

	/**
	 * @return a String representing the stock in the format:
	 *         [SYMBOL:OPEN,CLOSE] where OPEN and CLOSE are rounded to the
	 *         nearest two decimal places as in "%.2f".
	 *         For example: "[AAPL: 176.23, 175.07]"
	 */
	@Override
	public abstract String toString( );
}
