package com.k2yt.igarashiuitask.exchange;

import java.io.IOException;

import javax.swing.JFrame;

public final class ExchangeViewerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private final ExchangeData mData;
    private final ChartPanel mChartPanel;
    
    public ExchangeViewerFrame(ExchangeData data) {
        super("ExchangeViewer");
        mData = data;
        mChartPanel = new ChartPanel(mData);
        mChartPanel.setSize(500, 400);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(mChartPanel);
    }
    
    public static void main(String[] args) {
        try {
            final String filePath = args.length > 0 ? args[0] : "data.csv";
            final ExchangeData data = new ExchangeData(filePath);
            final ExchangeViewerFrame frame = new ExchangeViewerFrame(data);
            frame.setVisible(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
