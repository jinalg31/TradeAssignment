package com.tradeassignment;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import com.tradeassignment.model.Trade;

/**
 * TradeManager class to validate and store trades.
 * 
 * @author jgohel
 *
 */
public class TradeManager {

	private static TradeManager tradeManager;

	private TreeMap<String, List<Trade>> trades = new TreeMap<>();

	/**
	 * Single instance to manage single store
	 * 
	 * @return
	 */
	public static synchronized TradeManager getInstance() {
		if (tradeManager == null) {
			tradeManager = new TradeManager();
		}
		return tradeManager;
	}

	public TreeMap<String, List<Trade>> getTrades() {
		return trades;
	}

	/**
	 * Adds the given trade object after making validations and checks.
	 * 
	 * @param tradeObj
	 * @throws Exception
	 */
	public void addTrade(Trade tradeObj) throws Exception {
		// Retrieve trade id
		String tradeId = tradeObj.getTradeId();

		// Retrieve the trades for the trade id
		List<Trade> tradeList = trades.get(tradeId);

		if (tradeList == null) {
			tradeList = new LinkedList<Trade>();
		}

		// Validate for version
		checkVersion(tradeObj, tradeList);

		// Check if trade is expired
		boolean isExpired = checkExpired(tradeObj);
		
		// If trade is not expired
		if (!isExpired) {
			// Add trade order
			addTrade(tradeObj, tradeList);
		}

	}

	/**
	 * Add trade to its corresponding list.
	 * 
	 * @param tradeObj
	 * @param tradeList
	 */
	private void addTrade(Trade tradeObj, List<Trade> tradeList) {

		int version = tradeObj.getVersion();
		Optional<Trade> tradeFromList = tradeList.stream().filter(i -> i.getVersion() == version).findAny();

		// If trade is present with the same version then remove it
		if (tradeFromList.isPresent()) {
			tradeList.remove(tradeFromList.get());
		}

		// Add trade
		tradeList.add(tradeObj);

		// IF the list for the trade id doesn't exist then add it to main map
		if (trades.get(tradeObj.getTradeId()) == null) {
			trades.put(tradeObj.getTradeId(), tradeList);
		}

	}

	/**
	 * Method to check if the trade is expired.
	 * 
	 * @param tradeObj
	 * @return
	 */
	private boolean checkExpired(Trade tradeObj) {
		if (tradeObj.getMaturityDate().before(Calendar.getInstance().getTime())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if the given trade is having lower version compared to existing trade
	 * versions for this tradeid.
	 * 
	 * @param tradeObj
	 * @param tradeList
	 * @throws Exception
	 */
	private void checkVersion(Trade tradeObj, List<Trade> tradeList) throws Exception {
		if (tradeList != null && !tradeList.isEmpty()) {
			int minVersion = tradeList.stream().mapToInt(i -> i.getVersion()).min().getAsInt();
			if (tradeObj.getVersion() < minVersion) {
				throw new Exception("Version is lower");
			}
		}

	}

	/**
	 * Find out expired trades and marked their expired status to "Y".
	 * 
	 */
	public void markExpiredTrades() {
		// For each trade id, set the expired trades
		Set<String> tradeIds = trades.keySet();
		for (String tradeId : tradeIds) {
			trades.get(tradeId).stream().filter(t -> checkExpired(t)).forEach(t -> t.setExpired("Y"));
		}
	}

	public void printTrades() {
		trades.keySet().stream().map(t -> trades.get(t)).forEach(System.out::println);
	}

}
