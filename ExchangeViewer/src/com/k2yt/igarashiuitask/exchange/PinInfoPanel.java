package com.k2yt.igarashiuitask.exchange;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public final class PinInfoPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/M/d");
    
    // data
    private final ExchangeData mData;
    private final int mIndex;
    
    // delegate
    private PinManager mPinManager;
    
    // view
    private final JPanel mLabelPanel;
    private final JLabel mDateLabel;
    private final JLabel mPriceLabel;
    private final JButton mButton;
    
    public PinInfoPanel(ExchangeData data, int index) {
        mData = data;
        mIndex = index;
        mLabelPanel = new JPanel();
        mDateLabel = new JLabel(getDateText());
        mPriceLabel = new JLabel(getPriceText());
        mButton = new JButton("削除");
        
        mLabelPanel.setLayout(new BoxLayout(mLabelPanel, BoxLayout.Y_AXIS));
        mLabelPanel.add(mDateLabel);
        mLabelPanel.add(mPriceLabel);
        mButton.addActionListener(this);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(mLabelPanel);
        add(Box.createRigidArea(new Dimension(10, 10)));
        add(mButton);
    }

    public void setPinManager(PinManager pinManager) {
        mPinManager = pinManager;
    }
    
    public String getDateText() {
        if (mData.getDate(mIndex) == null) return "";
        return FORMAT.format(mData.getDate(mIndex));
    }

    public String getPriceText() {
        if (mData.getPrice(mIndex) == null) return "";
        return mData.getPrice(mIndex) + " 円/ドル";
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == mButton) {
            if (mPinManager != null) {
                mPinManager.removePin(mIndex);
            }
        }
    }
}
