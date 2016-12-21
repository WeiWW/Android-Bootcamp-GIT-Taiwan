package com.tobey.nytimessearch;

import java.util.Date;

/**
 * Created by tobeyyang on 16/12/2016.
 */

public class Filter {
    String  _beginDate;
    String _sortingOrder;
    String _newsType;

    public Filter(){

    }

    public Filter(String  _beginDate, String _sortingOrder, String _newsType) {
        this._beginDate = _beginDate;
        this._sortingOrder = _sortingOrder;
        this._newsType = _newsType;
    }

    public String  get_beginDate() {
        return _beginDate;
    }

    public void set_beginDate(String  _beginDate) {
        this._beginDate = _beginDate;
    }

    public String get_sortingOrder() {
        return _sortingOrder;
    }

    public void set_sortingOrder(String _sortingOrder) {
        this._sortingOrder = _sortingOrder;
    }

    public String get_newsType() {
        return _newsType;
    }

    public void set_newsType(String _newsType) {
        this._newsType = _newsType;
    }
}
