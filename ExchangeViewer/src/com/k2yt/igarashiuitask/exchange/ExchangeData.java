package com.k2yt.igarashiuitask.exchange;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExchangeData {
    private final List<Double> mPrices;
    private final List<Date> mDates;
    private final Map<Date, Integer> mIndexOfDate;
    private int mMinYear;
    private int mMaxYear;
    
    public ExchangeData(String filePath) throws IOException {
        mPrices = new ArrayList<Double>();
        mDates = new ArrayList<Date>();
        mIndexOfDate = new HashMap<Date, Integer>();
        mMinYear = 0;
        mMaxYear = 0;
        read(filePath);
    }
    
    public Double getPrice(int index) {
        if (index < 0 || index >= size()) return null;
        return mPrices.get(index);
    }
    
    public Date getDate(int index) {
        if (index < 0 || index >= size()) return null;
        return mDates.get(index);
    }
    
    public int size() {
        return mPrices.size();
    }
    
    public int minYear() {
        return mMinYear;
    }
    
    public int maxYear() {
        return mMaxYear;
    }
    
    public Integer getFirstIndexOfYear(int y) {
        final SimpleDateFormat fmt = new SimpleDateFormat("y/M/d");
        try {
            final Date d = fmt.parse(y + "/1/1");
            return mIndexOfDate.get(d);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void read(String filePath) throws IOException {
        final SimpleDateFormat fmt = new SimpleDateFormat("y/M/d");
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath)));
        reader.readLine();
        mMinYear = Integer.MAX_VALUE;
        mMaxYear = Integer.MIN_VALUE;
        String s;
        try {
            while ((s = reader.readLine()) != null) {
                final String[] ws = s.split(",");
                if (ws.length > 2) continue;
                final Date d = fmt.parse(ws[0]);
                mDates.add(d);
                final String ps = ws.length > 1 ? ws[1].trim() : "";
                mPrices.add(ps.length() > 0 ? new Double(ps) : null);
                final Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                final int y = cal.get(Calendar.YEAR);
                mMinYear = Math.min(mMinYear, y);
                mMaxYear = Math.max(mMaxYear, y);
                mIndexOfDate.put(d, mDates.size()-1);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            throw new IOException("invalie file format");
        }
        if (mPrices.size() == 0) {
            throw new IOException("invalid file format");
        }
    }
}
