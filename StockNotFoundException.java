package sample;

public class StockNotFoundException extends Exception {
	public StockNotFoundException( String symbol ) {
		super( String.format ( "Stock Not Found: %s", symbol ) );
	}
}
