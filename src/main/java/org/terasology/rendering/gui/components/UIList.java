package org.terasology.rendering.gui.components;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.terasology.rendering.gui.framework.IClickListener;
import org.terasology.rendering.gui.framework.IInputDataElement;
import org.terasology.rendering.gui.framework.UIScrollableDisplayContainer;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple graphical List
 *
 * @author Anton Kireev <adeon.k87@gmail.com>
 * @version 0.1
 */

public class UIList extends UIScrollableDisplayContainer implements IInputDataElement {

    private int _selectedItemIndex = -1;
    private final ArrayList<IClickListener> _doubleClickListeners = new ArrayList<IClickListener>();

    //List items
    private List<UIListItem> _items = new ArrayList<UIListItem>();

    public UIList(Vector2f size) {
        setSize(size);
        setCrop(true);
        setScrollBarsPosition(getPosition(), getSize());

        //ToDo Create skin for UIList
        setStyle("border-image-top", "engine:gui_menu 159/512 18/512 264/512 0 18");
        setStyle("border-image-right", "engine:gui_menu 9/512 63/512 423/512 18/512 9");
        setStyle("border-image-bottom", "engine:gui_menu 159/512 9/512 264/512 81/512 9");
        setStyle("border-image-left", "engine:gui_menu 8/512 64/512 256/512 17/512 8");

        setStyle("border-corner-topleft", "engine:gui_menu 256/512 0");
        setStyle("border-corner-topright", "engine:gui_menu 423/512 0");
        setStyle("border-corner-bottomright", "engine:gui_menu 423/512 81/512");
        setStyle("border-corner-bottomleft", "engine:gui_menu 256/512 81/512");

        setStyle("background-image", "engine:gui_menu 159/512 63/512 264/512 18/512");
        setCropMargin(new Vector4f(-15f, -15f, -15f, 0));
    }

    public void update() {
        Vector2f mousePos = new Vector2f(Mouse.getX(), Display.getHeight() - Mouse.getY());
        if (intersects(mousePos)) {

            if (_scrollBarVertical.intersects(mousePos) || _scrollBarHorizontal.intersects(mousePos)) {
                return;
            }

            boolean itemClicked = false;
            for (int i = (_items.size() - 1); i >= 0; i--) {
                UIListItem item = _items.get(i);
                if (item.isVisible()) {
                    if (item.intersects(mousePos)) {
                        if (_mouseDown) {
                            if (item.isSelected()) {
                                doubleClick();
                                break;
                            }
                            if (_selectedItemIndex >= 0) {
                                _items.get(_selectedItemIndex).setSelected(false);
                            }
                            item.setSelected(true);
                            _selectedItemIndex = i;
                            _mouseDown = false;
                            itemClicked = true;
                        }
                    }
                }
            }
            if (!itemClicked) {
                _mouseUp = false;
                _mouseDown = false;
            }

        } else {
            _wheelMoved = 0;
            _mouseUp = false;
            _mouseDown = false;
        }
        super.update();
    }

    public void render() {
        super.render();
    }

    private void doubleClick() {
        for (int i = 0; i < _doubleClickListeners.size(); i++) {
            _doubleClickListeners.get(i).clicked(this);
        }
    }

    /*
    * Returns count of elements
    */
    public int size() {
        return _items.size();
    }


    public void addItem(String text, Object value) {

        UIListItem newItem = new UIListItem(new Vector2f(getSize().x, (32f)), text, value);

        newItem.setVisible(true);

        if (_items.size() > 0) {
            newItem.setPosition(_items.get(0).getPosition());
        }

        newItem.getPosition().y += 32f * _items.size();
        newItem.setFixed(false);

        _items.add(newItem);
        addDisplayElement(newItem);
    }

    /*
    * Remove selected item
    */
    public void removeSelectedItem() {

        if (_selectedItemIndex < 0) {
            return;
        }

        removeDisplayElement(_items.get(_selectedItemIndex));
        _items.remove(_selectedItemIndex);

        for (int i = _selectedItemIndex; i < _items.size(); i++) {
            _items.get(i).getPosition().y -= 32f;
        }

        if (_selectedItemIndex > _items.size() - 1) {
            if (_items.size() - 1 >= 0) {
                _selectedItemIndex = _items.size() - 1;
                _items.get(_selectedItemIndex).setSelected(true);
            } else {
                _selectedItemIndex = -1;
            }
        }

        if (_selectedItemIndex >= 0) {
            _items.get(_selectedItemIndex).setSelected(true);
        }

    }

    /*
     * Return selected item
     */
    public UIListItem getSelectedItem() {

        if (_selectedItemIndex < 0) {
            return null;
        }

        return _items.get(_selectedItemIndex);
    }

    /*
     * Remove all items
     */
    public void removeAll() {
        clearData();
        for (int i = (_items.size() - 1); i >= 0; i--) {
            removeDisplayElement(_items.get(i));
            _items.remove(i);
        }
    }

    /*
    * Remove item by index
    */
    public void removeItem(int index) {
        removeDisplayElement(_items.get(index));
        _items.remove(index);
    }

    /*
    * Returns the value of the selected item
    */
    public Object getValue() {
        return _items.get(_selectedItemIndex).getValue();
    }

    /*
     * Reset to selected element
     */
    public void clearData() {
        if (_selectedItemIndex < 0) {
            return;
        }
        _items.get(_selectedItemIndex).setSelected(false);
        _selectedItemIndex = -1;
    }

    public void addDoubleClickListener(IClickListener listener) {
        _doubleClickListeners.add(listener);
    }

    public void removeDoubleClickListener(IClickListener listener) {
        _doubleClickListeners.remove(listener);
    }
}