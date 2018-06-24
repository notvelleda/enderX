package xyz.pugduddly.enderX.ui;

/**
 * Component wrapper that provides focus support for enderX.
 * This needs to be used in place of a standard AWT {@link java.awt.Component} if you want focus support.
 */
public class Component extends java.awt.Component {
    private boolean focus = false;
    private Window parentWindow = null;
    
    /**
     * Returns true if this `Component` is the focus owner.
     * @return `true` if this Component is the focus owner; `false` otherwise
     */
    @Override
    public boolean hasFocus() {
        return this.focus;
    }
    
    /**
     * Returns true if this `Component` is the focus owner.
     * @return `true` if this Component is the focus owner; `false` otherwise
     */
    @Override
    public boolean isFocusOwner() {
        return this.focus;
    }
    
    /**
     * Requests that this Component get the input focus.
     * This component must be added to a {@link xyz.pugduddly.enderX.ui.Window} in order for this request to be successful.
     */
    @Override
    public void requestFocus() {
        if (this.parentWindow != null)
            this.focus = this.parentWindow.requestComponentFocus(this);
    }
    
    /**
     * Removes focus from this Component.
     */
    public void removeFocus() {
        this.focus = false;
    }
    
    /**
     * Sets the parent Window of this component to the parameter `win`.
     * This method is called automatically upon being added to a Window.
     * @param win The window to be set as this Component's parent window.
     */
    public void setParentWindow(Window win) {
        this.parentWindow = win;
    }
}
