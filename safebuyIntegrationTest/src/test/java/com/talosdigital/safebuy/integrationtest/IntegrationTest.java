package com.talosdigital.safebuy.integrationtest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.talosdigital.safebuy.properties.Properties;
import com.talosdigital.safebuy.util.dto.BuyerDto;
import com.talosdigital.safebuy.util.dto.StoreDto;

public class IntegrationTest {

	public ArrayList<Integer> createdStores;
	public ArrayList<Integer> createdBuyers;
	
	@Before
	public void init(){
		createdStores = new ArrayList<Integer>();
		createdBuyers = new ArrayList<Integer>();
	}
	
	@Test
	public void testCreateBuyer(){
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			RestTemplate template = new RestTemplate();		    
			JSONObject buyer = new JSONObject();
			buyer.accumulate("name", "Juan David");
			buyer.accumulate("lastName", "Henao Zapata");
			HttpEntity<String> requestEntity = new HttpEntity<String>(
					buyer.toString() ,headers);
			
			ResponseEntity<BuyerDto> entity = template.postForEntity(
					Properties.URL + "rest/buyer", requestEntity, BuyerDto.class);
			
			assertEquals(HttpStatus.CREATED, entity.getStatusCode());
			
			if (entity.getStatusCode() == HttpStatus.CREATED) {
			  BuyerDto createdBuyer = entity.getBody();
			  createdBuyers.add(createdBuyer.getId());
			}
	}
	
	@Test
	public void testCreateStore(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate template = new RestTemplate();
		JSONObject store = new JSONObject();
		store.accumulate("name", "Kuzco");
		store.accumulate("nit", "23151");
		HttpEntity<String> requestEntity =new HttpEntity<String>(
				store.toString(), headers);
		
		ResponseEntity<StoreDto> entity = template.postForEntity(
				Properties.URL +"rest/store", requestEntity, StoreDto.class);
		
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		if (entity.getStatusCode() == HttpStatus.CREATED) {
			StoreDto createdStore = entity.getBody();
			createdStores.add(createdStore.getId());
		}
	}
	
	@After
	public void deleteCreatedRecords(){
		RestTemplate template = new RestTemplate();
		for (Integer id : createdBuyers) {
			template.delete(Properties.URL + "rest/buyer/" + id);
		}
		for (Integer id : createdStores) {
			template.delete(Properties.URL +"rest/store/" + id);
		}
	}
}
