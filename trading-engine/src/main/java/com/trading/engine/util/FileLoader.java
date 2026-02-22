package com.trading.engine.util;

import com.trading.engine.model.Trade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;

public class FileLoader {

    public static void load(String filePath, BlockingQueue<Trade> queue) throws Exception {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {

                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("trade")) {
                        continue;
                    }
                }

                if (line.trim().isEmpty()) continue;

                String[] parts = line.replace("\"", "").split(",");

                Trade trade = new Trade(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Integer.parseInt(parts[3].trim()),
                        Double.parseDouble(parts[4].trim()),
                        parts[5].trim()
                );

                queue.put(trade);
            }
        }
    }
}
