package edu.sjsu.cmpe.procurement.domain;

import java.util.ArrayList;

public class OrderBook {
    private String id;
    private ArrayList<Integer> order_book_isbns;
    
    public OrderBook(){
    	order_book_isbns = new ArrayList<Integer>();
    	this.id = "75829";
    	order_book_isbns.add(1);
    	order_book_isbns.add(2);
    }
    
    public String getId() {
    	return id;
    }

    public void setId(String id) {
    	this.id = id;
    }
    
    public ArrayList<Integer> getOrder_book_isbns() {
    	return order_book_isbns;
    }

    public void setOrder_book_isbns(Integer isbn) {
    	order_book_isbns.add(isbn);
    }    
}
