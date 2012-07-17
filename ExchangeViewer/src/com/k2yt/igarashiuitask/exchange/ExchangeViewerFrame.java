package com.k2yt.igarashiuitask.exchange;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public final class ExchangeViewerFrame extends JFrame implements ActionListener, PinManager {
    private static final long serialVersionUID = 1L;
    private static final int LEFT_PANE_WIDTH = 150;
    
    private final ExchangeData mData;
    
    private final JSplitPane mSplitPane;
    private final JPanel mLeftPanel;
    private final JButton mYearButton;
    private final JButton mMonthButton;
    private final JButton mDayButton;
    private final JPanel mScrollPanel;
    private final ChartPanel mChartPanel;
    private final Map<Integer, PinInfoPanel> mPinInfos;
    
    public ExchangeViewerFrame(ExchangeData data) {
        super("ExchangeViewer");
        mData = data;
        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mLeftPanel = new JPanel(new FlowLayout());
        mYearButton = new JButton("年単位表示");
        mMonthButton = new JButton("月単位表示");
        mDayButton = new JButton("日単位表示");
        mScrollPanel = new JPanel();
        mScrollPanel.setLayout(new BoxLayout(mScrollPanel, BoxLayout.Y_AXIS));
        mChartPanel = new ChartPanel(mData, ChartPanel.Mode.YEAR);
        mPinInfos = new HashMap<Integer, PinInfoPanel>();
        
        mYearButton.addActionListener(this);
        mMonthButton.addActionListener(this);
        mDayButton.addActionListener(this);
        
        JScrollPane scrollPane = new JScrollPane(mScrollPanel);
        scrollPane.setSize(LEFT_PANE_WIDTH, 0);
        mScrollPanel.add(new JLabel("右クリックで追加"));
        mLeftPanel.setMinimumSize(new Dimension(0, 0));
        mLeftPanel.add(mYearButton);
        mLeftPanel.add(mMonthButton);
        mLeftPanel.add(mDayButton);
        mLeftPanel.add(scrollPane);
        mSplitPane.setLeftComponent(mLeftPanel);
        mChartPanel.setPinManager(this);
        mSplitPane.setRightComponent(mChartPanel);
        mSplitPane.setDividerLocation(LEFT_PANE_WIDTH);
        getContentPane().add(mSplitPane);
        
        setSize(1000, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMode(ChartPanel.Mode.YEAR);
    }
    
    private void setMode(ChartPanel.Mode mode) {
        mChartPanel.setMode(mode);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == mYearButton) {
            setMode(ChartPanel.Mode.YEAR);
        }
        else if (ae.getSource() == mMonthButton) {
            setMode(ChartPanel.Mode.MONTH);
        }
        else if (ae.getSource() == mDayButton) {
            setMode(ChartPanel.Mode.DAY);
        }
    }
    
    @Override
    public void addPin(int index) {
        final PinInfoPanel pinInfoPanel = new PinInfoPanel(mData, index);
        pinInfoPanel.setPinManager(this);
        mChartPanel.addPin(index);
        mScrollPanel.add(pinInfoPanel, 0);
        mPinInfos.put(index, pinInfoPanel);
        validate();
        repaint();
    }

    @Override
    public void removePin(int index) {
        mChartPanel.removePin(index);
        mScrollPanel.remove(mPinInfos.get(index));
        mPinInfos.remove(index);
        validate();
        repaint();
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
