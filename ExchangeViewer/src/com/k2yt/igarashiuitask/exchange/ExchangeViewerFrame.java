package com.k2yt.igarashiuitask.exchange;

import javax.swing.JFrame;

public final class ExchangeViewerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private final ExchangeData mData;
    private final ChartPanel mChartPanel;
    
    public ExchangeViewerFrame(String filePath) {
        super("ExchangeViewer");
        mData = new ExchangeData(filePath);
        mChartPanel = new ChartPanel();
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(mChartPanel);
    }
    
    public static void main(String[] args) {
        ExchangeViewerFrame frame = new ExchangeViewerFrame("data.csv");
        frame.setVisible(true);
    }
}
