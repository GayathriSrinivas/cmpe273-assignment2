package edu.sjsu.cmpe.library.ui.views;

import java.util.List;

import com.yammer.dropwizard.views.View;

import edu.sjsu.cmpe.library.domain.Book;

public class HomeView extends View {
    private final List<Book> books;

    public HomeView(List<Book> books) {
	super("home.mustache");
	this.books = books;
    }

    public List<Book> getBook() {

    	for (int i = 0; i < books.size(); i++) {
			if(books.get(i).getStatus() == Book.Status.lost)
				books.get(i).setDisabled("disabled");
			else 
				books.get(i).setDisabled("");
		}
	return books;
    }
}
