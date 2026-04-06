package edu.cit.chan.restoradar.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SortStrategyFactory {
    
    private final Map<String, SortStrategy> strategies;

    public SortStrategyFactory(Map<String, SortStrategy> strategies) {
        this.strategies = strategies;
    }

    public SortStrategy getStrategy(String sortType) {
        if (sortType == null || sortType.isBlank()) {
            return strategies.get("nearest"); // default algorithm
        }

        SortStrategy strategy = strategies.get(sortType);
        if (strategy == null) {
            return strategies.get("nearest"); // default algorithm fallback
        }
        
        return strategy;
    }
}
