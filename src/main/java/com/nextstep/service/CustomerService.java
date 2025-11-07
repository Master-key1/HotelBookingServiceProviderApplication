package com.nextstep.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nextstep.model.Customer;
import com.nextstep.repo.CustomerRepo;

@Service
public class CustomerService {
	
	private CustomerRepo repo;
	
	public Customer  saveCustomer(Customer customer) {
		return repo.save(customer);
	}
	
	public List<Customer> getallCustomer(){
		return repo.findAll();
		
	}
	public  Customer getById(String custId) {
		return repo.findById(custId).orElse(null);
	}
	public String deleteCusomer(String CustId) {
		
		repo.deleteById(CustId);
		return "Customer Deleted Successfully";
	}
}
