package hu.webuni.booking.web;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.bonus.api.BonusApi;
import hu.webuni.booking.dto.PurchaseData;
import hu.webuni.booking.dto.TicketData;
import hu.webuni.currency.api.CurrencyApi;
import hu.webuni.flights.api.FlightsApi;
import hu.webuni.flights.dto.Airline;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Value("${booking.bonus}")
    double bonusRate;
    
    @Autowired
    BonusApi bonusApi;
    @Autowired
    CurrencyApi currencyApi;
    @Autowired
    FlightsApi flightsApi;

    @PostMapping("/ticket")
    public PurchaseData buyTicket(@RequestBody TicketData ticketData) {
    	PurchaseData pd=new PurchaseData();
    	pd.setSuccess(false);
    	List<Airline> searchFlight = flightsApi.searchFlight(ticketData.getFrom(), ticketData.getTo());
    	if (searchFlight.isEmpty()) {
    		return pd;
    	}
    	searchFlight.forEach(a -> {
    		if (!a.getCurrency().equals("USD")) {
    			a.setPrice(currencyApi.getRate(a.getCurrency(), "USD") * a.getPrice());
    			a.setCurrency("USD");
    		}
    	});
    	Airline cheapestAirline = searchFlight.stream().min(Comparator.comparingDouble(Airline::getPrice)).get();
    	if (ticketData.isUseBonus()) {
    		double userPoints = bonusApi.getPoints(ticketData.getUser());
    		double usedBonusPoints = (cheapestAirline.getPrice()>userPoints)?userPoints:cheapestAirline.getPrice();
    		//levonjuk a usertől a használt bónuszpontokat
    		bonusApi.addPoints(ticketData.getUser(), -usedBonusPoints);
    		pd.setBonusUsed(usedBonusPoints);
    		pd.setPrice(cheapestAirline.getPrice()-usedBonusPoints);
    	}
    	
    	//bónuszpontok jóváírása
    	double bonusEarned = cheapestAirline.getPrice()*bonusRate;
    	bonusApi.addPoints(ticketData.getUser(), bonusEarned);
    	pd.setBonusEarned(bonusEarned);
    	
    	pd.setSuccess(true);
    	//git test
    	
        return pd;
    }
    
    @GetMapping("/test")
    public void test() {
    	bonusApi.addPoints("Elek", 4);
    	
    }
}
