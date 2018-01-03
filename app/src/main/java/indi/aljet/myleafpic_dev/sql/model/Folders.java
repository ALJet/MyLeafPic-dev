package indi.aljet.myleafpic_dev.sql.model;

import org.litepal.crud.DataSupport;

/**
 * Created by PC-LJL on 2017/11/15.
 */

public class Folders extends DataSupport {

    private   String path;
    private   int id;
    private   int pinned;
    private   String cover_path;
    private   int status;
    private   int sorting_mode;
    private   int sorting_order;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPinned() {
        return pinned;
    }

    public void setPinned(int pinned) {
        this.pinned = pinned;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSorting_mode() {
        return sorting_mode;
    }

    public void setSorting_mode(int sorting_mode) {
        this.sorting_mode = sorting_mode;
    }

    public int getSorting_order() {
        return sorting_order;
    }

    public void setSorting_order(int sorting_order) {
        this.sorting_order = sorting_order;
    }
}
