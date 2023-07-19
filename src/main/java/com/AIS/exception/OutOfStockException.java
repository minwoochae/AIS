package com.AIS.exception;


public class OutOfStockException extends RuntimeException{
	
	//분양 신청 되어 있으면  발생되는 exception
	public OutOfStockException(String Message) {
		super(Message);
	}
}
