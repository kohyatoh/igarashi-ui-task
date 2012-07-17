package com.k2yt.igarashiuitask.exchange;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PinInfoPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/M/d");
    
    // data
    private final ExchangeData mData;
    private final int mIndex;
    
    // interface
    private PinManager mPinManager;
    
    // view
    private final JLabel mLabel;
    private final JButton mButton;
    
    public PinInfoPanel(ExchangeData data, int index) {
        super(new FlowLayout());
        mData = data;
        mIndex = index;
        mLabel = new JLabel(getText());
        mButton = new JButton();
        
        mButton.addActionListener(this);
        add(mLabel);
        add(mButton);
    }

    public void setPinManager(PinManager pinManager) {
        mPinManager = pinManager;
    }
    
    public String getText() {
        if (mData.getDate(mIndex) == null) return "";
        return FORMAT.format(mData.getDate(mIndex)) + ": " + mData.getPrice(mIndex);
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
