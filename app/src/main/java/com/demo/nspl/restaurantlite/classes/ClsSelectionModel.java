package com.demo.nspl.restaurantlite.classes;

public class ClsSelectionModel {

    private String _character = "";

    private boolean isSelected;

    public ClsSelectionModel() {

    }
  public ClsSelectionModel(String _character) {
        this._character = _character;
    }

    public ClsSelectionModel(String _character, boolean isSelected) {
        this._character = _character;
        this.isSelected = isSelected;
    }

    public String get_character() {
        return _character;
    }

    public void set_character(String _character) {
        this._character = _character;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
