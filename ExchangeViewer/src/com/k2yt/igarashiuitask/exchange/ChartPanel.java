package com.k2yt.igarashiuitask.exchange;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public final class ChartPanel extends JPanel implements MouseMotionListener {
    private static final long serialVersionUID = 1L;
    
    public enum Mode {
        YEAR, MONTH, DAY
    }
    
    // associated data
    private final ExchangeData mData;
    private final Set<Integer> mPinned;
    
    // view parameter
    private int mMaxValue = 175;
    private int mMinValue = 55;
    private double mInterval;
    private Mode mMode;
    
    // view state
    private int mCenterIndex;
    private int mMouseLastX;

    public ChartPanel(ExchangeData data, Mode mode) {
        mData = data;
        mPinned = new HashSet<Integer>();
        mCenterIndex = 0;
        mMouseLastX = -1;
        addMouseMotionListener(this);
        setMode(mode);
    }

    public void addPin(int index) {
        mPinned.add(index);
    }
    
    public void removePin(int index) {
        mPinned.remove(index);
    }
    
    public void setMode(Mode mode) {
        switch (mode) {
        case YEAR:
            mInterval = 0.25;
            break;
        case MONTH:
            mInterval = 3;
            break;
        case DAY:
            mInterval = 25;
            break;
        }
        mMode = mode;
        repaint();
    }

    public int getIndexFromX(int x, boolean hasValue) {
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
    
    public int getXFromIndex(int index) {
        return (int)((index - mCenterIndex) * mInterval) + getWidth() / 2;
    }
    
    public int getYFromValue(double value) {
        final double r = (mMaxValue - value) / (mMaxValue - mMinValue);
        return (int)(getHeight() * r);
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
        for (int v = mMinValue / 10 * 10; v <= mMaxValue; v += 10) {
            final int y = getYFromValue(v);
            g.setColor(Color.gray);
            g.drawString(v + " 円/ドル", 0, y);
            g.setColor(Color.lightGray);
            g.drawLine(0, y, getWidth(), y);
        }
        for (int y = mData.minYear(); y <= mData.maxYear(); y++) {
            final int index = mData.getFirstIndexOfYear(y);
            final int x = getXFromIndex(index);
            g.setColor(Color.gray);
            g.drawString(y + "年", x, getHeight()-15);
            g.setColor(Color.lightGray);
            g.drawLine(x, 0, x, getHeight());
        }
        if (mMode == Mode.YEAR) return;
        for (int y = mData.minYear(); y <= mData.maxYear(); y++) {
            for (int m = 1; m <= 12; m++) {
                if (mData.getFirstIndexOfMonth(y, m) == null) continue;
                final int index = mData.getFirstIndexOfMonth(y, m);
                final int x = getXFromIndex(index);
                g.setColor(Color.gray);
                g.drawString(m + "月", x, getHeight()-8);
                g.setColor(Color.lightGray);
                g.drawLine(x, 0, x, getHeight());
            }
        }
        if (mMode == Mode.MONTH) return;
        for (int y = mData.minYear(); y <= mData.maxYear(); y++) {
            for (int m = 1; m <= 12; m++) {
                for (int d = 5; d <= 30; d += 5) {
                    if (mData.existsDay(y, m, d) == false) continue;
                    final int index = mData.getIndexOfDay(y, m, d);
                    final int x = getXFromIndex(index);
                    g.setColor(Color.gray);
                    g.drawString(d + "日", x, getHeight()-8);
                    g.setColor(Color.lightGray);
                    g.drawLine(x, 0, x, getHeight());
                }
            }
        }
    }
    
    private void paintGraph(Graphics g) {
        g.setColor(Color.black);
        for (int i = 0; i < mData.size(); i++) {
            if (mData.getPrice(i) == null) continue;
            final int xi = getXFromIndex(i);
            final int yi = getYFromValue(mData.getPrice(i));
            if (xi < Short.MIN_VALUE || xi > Short.MAX_VALUE) continue;
            for (int j = i+1; j < mData.size(); j++) {
                if (mData.getPrice(j) == null) continue;
                final int xj = getXFromIndex(j);
                final int yj = getYFromValue(mData.getPrice(j));
                if (xj < Short.MIN_VALUE || xj > Short.MAX_VALUE) continue;
                if (mInterval > 10) g.fillOval(xi-2, yi-2, 5, 5);
                if (mPinned.contains(i)) {
                    g.setColor(Color.red);
                    g.fillOval(xi-3, yi-3, 7, 7);
                    g.setColor(Color.black);
                }
                g.drawLine(xi, yi, xj, yj);
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
    
    private void updateMouseState(MouseEvent me) {
        mMouseLastX = me.getX();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (mMouseLastX >= 0 && mMouseLastX < getWidth()) {
            final double diff = (mMouseLastX - me.getX()) / mInterval;
            if (Math.abs(diff) < 1) return ;
            mCenterIndex += diff;
        }
        updateMouseState(me);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        updateMouseState(me);
        repaint();
    }
}
