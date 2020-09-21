package com.demo.nspl.restaurantlite.classes;

import java.util.HashSet;
import java.util.List;

public class ClsItemFilter {

    public static class Layer {
        public int getLayerID() {
            return layerID;
        }

        public void setLayerID(int layerID) {
            this.layerID = layerID;
        }

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public List<LayerItem> getLayerItemList() {
            return layerItemList;
        }

        public void setLayerItemList(List<LayerItem> layerItemList) {
            this.layerItemList = layerItemList;
        }

        int layerID;
        String layerName;
        boolean selected;
        List<LayerItem> layerItemList;

    }

    public static class LayerItem {
        int itemID;
        String itemName;
        boolean selected;  int layerID;

        public int getItemID() {
            return itemID;
        }
        public int getLayerID() {
            return layerID;
        }

        public void setLayerID(int layerID) {
            this.layerID = layerID;
        }

        public void setItemID(int itemID) {
            this.itemID = itemID;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }


        @Override
        public String toString() {
            return "LayerItem{" +
                    "itemID=" + itemID +
                    ", itemName='" + itemName + '\'' +
                    ", selected=" + selected +
                    ", layerID=" + layerID +
                    '}';
        }
    }
}