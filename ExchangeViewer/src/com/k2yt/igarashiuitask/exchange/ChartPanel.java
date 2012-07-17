package com.k2yt.igarashiuitask.exchange;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

public final class ChartPanel extends JPanel implements MouseMotionListener {
    private static final long serialVersionUID = 1L;
    
    // associated data
    private final ExchangeData mData;
    
    // view parameter
    private int mMaxValue = 175;
    private int mMinValue = 55;
    private int mInterval = 1;
    
    // view state
    private int mCenterIndex;
    private int mMouseLastX;

    public ChartPanel(ExchangeData data) {
        mData = data;
        mCenterIndex = 0;
        mMouseLastX = -1;
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        paintFrame(g);
        paintGraph(g);
        paintCurrentPoint(g);
    }

    private void paintFrame(Graphics g) {
        g.setColor(Color.lightGray);
        for (int v = mMinValue / 10 * 10; v <= mMaxValue; v += 10) {
            final int y = getYFromValue(v);
            g.drawString("" + v, 0, y);
            g.drawLine(0, y, getWidth(), y);
        }
        for (int y = mData.minYear(); y <= mData.maxYear(); y++) {
            final int index = mData.getFirstIndexOfYear(y);
            final int x = getXFromIndex(index);
            g.drawLine(x, 0, x, getHeight());
            g.drawString("" + y, x, getHeight()-5);
        }
    }
    
    private void paintGraph(Graphics g) {
        g.setColor(Color.black);
        for (int i = 0; i < mData.size(); i++) {
            if (mData.getPrice(i) == null) continue;
            for (int j = i+1; j < mData.size(); j++) {
                if (mData.getPrice(j) == null) continue;
                g.drawLine(getXFromIndex(i), getYFromValue(mData.getPrice(i)),
                        getXFromIndex(j), getYFromValue(mData.getPrice(j)));
                break;
            }
        }
    }
    
    private void paintCurrentPoint(Graphics g) {
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d");
        final int curIndex = getIndexFromX(mMouseLastX, true);
        g.setColor(Color.black);
        g.drawString(fmt.format(mData.getDate(curIndex)), getWidth() - 125, 10);
        g.drawString("" + mData.getPrice(curIndex), getWidth() - 50, 10);
        g.setColor(Color.blue);
        final int x = getXFromIndex(curIndex);
        final int y = getYFromValue(mData.getPrice(curIndex));
        g.fillOval(x-3, y-3, 7, 7);
    }
    
    private int getIndexFromX(int x, boolean hasValue) {
        int index = -1, dx = Integer.MAX_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            if (hasValue && mData.getPrice(i) == null) continue;
            if (Math.abs(getXFromIndex(i) - mMouseLastX) < dx) {
                dx = Math.abs(getXFromIndex(i) - mMouseLastX);
                index = i;
            }
        }
        return index;
    }
    
    private int getXFromIndex(int index) {
        return (index - mCenterIndex) * mInterval + getWidth() / 2;
    }
    
    private int getYFromValue(double value) {
        final double r = (mMaxValue - value) / (mMaxValue - mMinValue);
        return (int)(getHeight() * r);
    }
    
    private void updateMouseState(MouseEvent me) {
        mMouseLastX = me.getX();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (mMouseLastX >= 0 && mMouseLastX < getWidth()) {
            mCenterIndex += mMouseLastX - me.getX();
        }
        updateMouseState(me);
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        updateMouseState(me);
        this.repaint();
    }
}
