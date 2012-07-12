package com.k2yt.igarashiuitask.exchange;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class ExchangeData {
    List<Double> mPrices;
    
    public ExchangeData(String filePath) {
        mPrices = new ArrayList<Double>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath)));
            reader.readLine();
            String s;
            while ((s = reader.readLine()) != null) {
                String[] ws = s.split(",");
                if (ws.length != 2) continue;
                ws[1] = ws[1].trim();
                mPrices.add(ws[1].length() > 0 ? new Double(ws[1]) : null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPrices.size() == 0) {
            System.err.println("Invalid data?");
        }
    }
}
