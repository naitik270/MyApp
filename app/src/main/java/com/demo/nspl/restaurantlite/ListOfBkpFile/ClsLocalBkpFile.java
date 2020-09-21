package com.demo.nspl.restaurantlite.ListOfBkpFile;

public class ClsLocalBkpFile {

    String _filePath = "";
    String _fileName = "";
    String _createDate = "";
    String _folderName = "";
    String _fileSize = "";


    public String get_folderName() {
        return _folderName;
    }

    public void set_folderName(String _folderName) {
        this._folderName = _folderName;
    }



    public String get_fileSize() {
        return _fileSize;
    }

    public void set_fileSize(String _fileSize) {
        this._fileSize = _fileSize;
    }



    public String get_createDate() {
        return _createDate;
    }

    public void set_createDate(String _createDate) {
        this._createDate = _createDate;
    }

    public String get_filePath() {
        return _filePath;
    }

    public void set_filePath(String _filePath) {
        this._filePath = _filePath;
    }

    public String get_fileName() {
        return _fileName;
    }

    public void set_fileName(String _fileName) {
        this._fileName = _fileName;
    }
}
