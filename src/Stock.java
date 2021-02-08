package sample;

public class Stock extends AbstractStock{
    /**
     * Constructor used to initialize a new instance of the stock
     *
     * @param symbol    : the ticker symbol of the stock (e.g. MSFT for Microsoft)
     * @param timestamp : 11:59pm on the trading day when the data was obtained
     * @param open      : the opening price of the stock
     * @param high      : the highest price at which the stock traded that day
     * @param low       : the lowest price at which the stock traded that day
     * @param close     : the price at the end of trading for the day
     * @param volume    : the number of shares traded that day
     */
    public Stock(String symbol, Long timestamp, double open, double high, double low, double close, double volume) {
        super(symbol, timestamp, open, high, low, close, volume);
    }

    /**
     * @return a String representing the stock in the format:
     * [SYMBOL:OPEN,CLOSE] where OPEN and CLOSE are rounded to the
     * nearest two decimal places as in "%.2f".
     * For example: "[AAPL: 176.23, 175.07]"
     */
    @Override
    public String toString() {
        return ("[" + this.getSymbol() + ":" + String.format("%.2f", this.getOpen()) + "," +
                String.format("%.2f", this.getClose()) + "]");
    }
}
