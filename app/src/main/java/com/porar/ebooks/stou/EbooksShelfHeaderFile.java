package com.porar.ebooks.stou;

import java.io.Serializable;

import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;

@SuppressWarnings("serial")
public class EbooksShelfHeaderFile implements Serializable {
	private Model_EBook_Shelf_List modelShelf;
	private Model_Customer_Detail modelCustomer;

	public EbooksShelfHeaderFile(Model_EBook_Shelf_List model, Model_Customer_Detail modelCustomer) {
		this.setModelShelf(model);
		this.setModelCustomer(modelCustomer);
	}

	public Model_EBook_Shelf_List getModelShelf() {
		return modelShelf;
	}

	public void setModelShelf(Model_EBook_Shelf_List modelShelf) {
		this.modelShelf = modelShelf;
	}

	public Model_Customer_Detail getModelCustomer() {
		return modelCustomer;
	}

	public void setModelCustomer(Model_Customer_Detail modelCustomer) {
		this.modelCustomer = modelCustomer;
	}
}
