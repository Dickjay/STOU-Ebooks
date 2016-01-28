package com.porar.ebooks.model;

/**
 * Created by Porar on 10/8/2015.
 */
public class Model_Phone_StatedButton {
    public boolean isAllEbooks = false;
    public boolean isFavoriteBoook = false;
    public boolean isThrashBook = false;
    public boolean isClickEvents = false;
    private boolean isDeletedThrashBook = false;

    public Model_Phone_StatedButton(boolean isAllEbooks, boolean isFavoriteBoook, boolean isThrashBook, boolean isClickEvents, boolean isDeletedThrashBook) {
        this.isAllEbooks = isAllEbooks;
        this.isFavoriteBoook = isFavoriteBoook;
        this.isThrashBook = isThrashBook;
        this.isClickEvents = isClickEvents;
        this.isDeletedThrashBook = isDeletedThrashBook;
    }

    public boolean getIsAllEbooks() {
        return isAllEbooks;
    }

    public void setIsAllEbooks(boolean isAllEbooks) {
        this.isAllEbooks = isAllEbooks;
    }

    public boolean getIsFavoriteBoook() {
        return isFavoriteBoook;
    }

    public void setIsFavoriteBoook(boolean isFavoriteBoook) {
        this.isFavoriteBoook = isFavoriteBoook;
    }

    public boolean getIsThrashBook() {
        return isThrashBook;
    }

    public void setIsThrashBook(boolean isThrashBook) {
        this.isThrashBook = isThrashBook;
    }

    public boolean getIsClickEvents() {
        return isClickEvents;
    }

    public void setIsClickEvents(boolean isClickEvents) {
        this.isClickEvents = isClickEvents;
    }

    public boolean getIsDeletedThrashBook() {
        return isDeletedThrashBook;
    }

    public void setIsDeletedThrashBook(boolean isDeletedThrashBook) {
        this.isDeletedThrashBook = isDeletedThrashBook;
    }


}
