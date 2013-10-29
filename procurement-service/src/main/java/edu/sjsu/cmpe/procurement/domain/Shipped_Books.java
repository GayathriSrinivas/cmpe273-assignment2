package edu.sjsu.cmpe.procurement.domain;

import java.util.ArrayList;

public class Shipped_Books {

	private ArrayList<Book> shipped_books;
	
	public Shipped_Books(){
		shipped_books = new ArrayList<Book>();
	}
	
	public void setShipped_books(Book[] book){
		for (int i = 0; i < book.length; i++) {
			System.out.println("BOOK "+ i);
			shipped_books.add(book[i]);
		}
	}
	
	public ArrayList<Book> getShipped_books(){
		return shipped_books;
	}
}
