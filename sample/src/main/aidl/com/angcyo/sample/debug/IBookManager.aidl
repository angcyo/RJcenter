// IBookManager.aidl
package com.angcyo.sample.debug;

// Declare any non-default types here with import statements

import com.angcyo.sample.debug.BookBean;

interface IBookManager {
    List<BookBean> getBookList();
    void addBook(in BookBean book);
}
