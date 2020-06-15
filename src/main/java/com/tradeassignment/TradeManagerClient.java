package com.tradeassignment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tradeassignment.model.Trade;

public class TradeManagerClient {
	public static void main(String[] args) throws Exception {
		TradeManager tradeManager = TradeManager.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date maturityDate = null;
		for (int i = 0; i < 100; i++) {
//			if(i % 5 == 0) {
//				maturityDate = dateFormat.parse("20/05/2020");
//			}else {
			maturityDate = dateFormat.parse("20/08/2020");
//			}

			tradeManager.addTrade(
					new Trade("T" + (i + 1), i, "CP-1", "B1", maturityDate, Calendar.getInstance().getTime(), "N"));

		}
		maturityDate = dateFormat.parse("20/05/2020");
		tradeManager.getTrades().get("T2").get(0).setMaturityDate(maturityDate);
		tradeManager.printTrades();
		
		Thread t = new Thread(() -> {
			tradeManager.markExpiredTrades();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t.start();
		t.join(5000);
		tradeManager.printTrades();
		
//		tradeManager.printTrades();
	}

}
