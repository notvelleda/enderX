package xyz.pugduddly.enderX.ui;

public class Component extends java.awt.Component {
    private boolean focus = false;
    private Window parentWindow = null;
    
    @Override
    public boolean hasFocus() {
        return this.focus;
    }
    
    @Override
    public void requestFocus() {
        if (this.parentWindow != null)
            this.focus = this.parentWindow.requestComponentFocus(this);
    }
    
    public void removeFocus() {
        this.focus = false;
    }
    
    public void setParentWindow(Window win) {
        this.parentWindow = win;
    }
}
