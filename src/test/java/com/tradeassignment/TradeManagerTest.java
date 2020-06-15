package com.tradeassignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.tradeassignment.model.Trade;

@TestMethodOrder(OrderAnnotation.class)
class TradeManagerTest {

	private TradeManager tradeManager;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@BeforeEach
	void setUp() throws Exception {
		tradeManager = TradeManager.getInstance();
	}

	@Test
	@Order(1)
	public void givenEmptyList_whenIsEmpty_thenTrueIsReturned() {
		TreeMap<String, List<Trade>> trades = tradeManager.getTrades();
		assertTrue(trades.isEmpty());
	}

	@Test
	@Order(2)
	void givenList_whenAddedTrade_thenListIsUpdated() throws Exception {
		TreeMap<String, List<Trade>> trades = tradeManager.getTrades();
		int listSize = (trades.get("T1") == null) ? 0 : trades.get("T1").size();
		Date maturityDate = dateFormat.parse("20/08/2020");
		tradeManager.addTrade(new Trade("T1", 1, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		assertEquals(listSize + 1, tradeManager.getTrades().get("T1").size());

	}

	@Test
	@Order(3)
	public void givenList_whenAddedTradeWithLowerVersion_getsRejected() {
		Date maturityDate;
		try {
			maturityDate = dateFormat.parse("20/08/2020");
			Exception thrown = assertThrows(Exception.class,
					() -> tradeManager.addTrade(
							new Trade("T1", 0, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N")),
					"Version is lower");
			assertTrue(thrown.getMessage().equals("Version is lower"));
		} catch (ParseException e) {

			e.printStackTrace();
		}

	}

	@Test
	@Order(4)
	void givenList_whenAddedTradeWithSameVersion_thenListIsUpdated() throws Exception {
		TreeMap<String, List<Trade>> trades = tradeManager.getTrades();
		int listSize = (trades.get("T1") == null) ? 0 : trades.get("T1").size();
		Date maturityDate = dateFormat.parse("25/08/2020");
		tradeManager.addTrade(new Trade("T1", 1, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		assertEquals(listSize, tradeManager.getTrades().get("T1").size());

	}

	@Test
	@Order(5)
	void givenList_whenAddedTradeWithEarlierMaturity_thenRejected() throws Exception {
		TreeMap<String, List<Trade>> trades = tradeManager.getTrades();
		Date maturityDate = dateFormat.parse("25/05/2020");
		tradeManager.addTrade(new Trade("T2", 1, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		assertNull(tradeManager.getTrades().get("T2"));
	}
	
	@Test
	@Order(6)
	void givenList_whenAddedTrades_thenMarkExpiredTrades() throws Exception {
		TreeMap<String, List<Trade>> trades = tradeManager.getTrades();
		
		Date maturityDate = dateFormat.parse("25/08/2020");
		tradeManager.addTrade(new Trade("T1", 1, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		
		maturityDate = dateFormat.parse("25/06/2020");
		tradeManager.addTrade(new Trade("T1", 2, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		
		maturityDate = dateFormat.parse("25/07/2020");
		tradeManager.addTrade(new Trade("T2", 1, "CP-2", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		
		maturityDate = dateFormat.parse("25/09/2020");
		tradeManager.addTrade(new Trade("T3", 1, "CP-3", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));
		
		tradeManager.getTrades().get("T2").get(0).setMaturityDate(dateFormat.parse("25/05/2020"));
		
		tradeManager.markExpiredTrades();
		
		tradeManager.printTrades();
		
		assertEquals("Y", tradeManager.getTrades().get("T2").get(0).isExpired());
	}

}
