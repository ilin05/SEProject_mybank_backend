package com.mybank.module5_foreign.Controller;

import com.mybank.module5_foreign.back.WHCommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/SearchCurrency")
public class WHSearchCurrencyController {

    private final WHCommonFunctions commonFunctions;

    @Autowired
    public WHSearchCurrencyController(WHCommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> getExchangeRates(@RequestBody CurrencyRequest request) {
        try {
            Map<String, Double> rates = commonFunctions.getExchangeRateByCurrency(request.getCurrencyId());
            String currencyName = commonFunctions.findCurrencyName(request.getCurrencyId());

            if (rates == null || rates.isEmpty() || currencyName == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("rates", rates);
            response.put("currencyName", currencyName);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

class CurrencyRequest {
    private int currencyId;

    public int getCurrencyId() { return currencyId; }
    public void setCurrencyId(int currencyId) { this.currencyId = currencyId; }
}

