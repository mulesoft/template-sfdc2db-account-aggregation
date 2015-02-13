/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.api.MuleContext;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.VariableNames;

@RunWith(MockitoJUnitRunner.class)
public class AccountsMergeTest {
	
	private List<Map<String, String>> accountsSalesforce;
	private List<Map<String, String>> accountsDatabase;
	
	@Mock
	private MuleContext muleContext;

	@Before
	public void setUp(){
		accountsSalesforce = prepareAccountsA();		
		accountsDatabase = prepareAccountsB();
	}

	static List<Map<String, String>> prepareAccountsB() {
		List<Map<String,String>> accountsDatabase = new ArrayList<Map<String,String>>();
		
		Map<String, String> account1Database = new HashMap<String, String>();
		account1Database.put(VariableNames.ID, "1");
		account1Database.put(VariableNames.NAME, "Generica");
		account1Database.put(VariableNames.INDUSTRY, "Experimental");
		account1Database.put(VariableNames.NUMBER_OF_EMPLOYEES, "500");
		accountsDatabase.add(account1Database);

		Map<String, String> account2Database = new HashMap<String, String>();
		account2Database.put("Id", "2");
		account2Database.put(VariableNames.NAME, "Global Voltage");
		account2Database.put("Industry", "Energetic");
		account2Database.put("NumberOfEmployees", "4160");
		accountsDatabase.add(account2Database);
		return accountsDatabase;
	}

	static List<Map<String,String>> prepareAccountsA() {
		List<Map<String,String>> accountsSalesforce = new ArrayList<Map<String,String>>();

		Map<String, String> account0Salesforce = new HashMap<String, String>();
		account0Salesforce.put(VariableNames.ID, "0");
		account0Salesforce.put(VariableNames.NAME, "Sony");
		account0Salesforce.put(VariableNames.INDUSTRY, "Entertaiment");
		account0Salesforce.put(VariableNames.NUMBER_OF_EMPLOYEES, "28");
		accountsSalesforce.add(account0Salesforce);

		Map<String, String> account1Salesforce = new HashMap<String, String>();
		account1Salesforce.put(VariableNames.ID, "1");
		account1Salesforce.put(VariableNames.NAME, "Generica");
		account1Salesforce.put(VariableNames.INDUSTRY, "Pharmaceutic");
		account1Salesforce.put(VariableNames.NUMBER_OF_EMPLOYEES, "22");
		accountsSalesforce.add(account1Salesforce);
		return accountsSalesforce;
	}
	
	@Test
	public void testMerge() throws TransformerException {
		
		
		AccountsMerge sfdcAccountMerge = new AccountsMerge();
		List<Map<String, String>> mergedList = sfdcAccountMerge.mergeList(accountsSalesforce, accountsDatabase);

		Assert.assertEquals("The merged list obtained is not as expected",
				createExpectedList(), mergedList);
	}

	static List<Map<String, String>> createExpectedList() {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put(VariableNames.ID_IN_SALESFORCE, "0");
		record0.put(VariableNames.ID_IN_DATABASE, "");
		record0.put(VariableNames.INDUSTRY_IN_SALESFORCE, "Entertaiment");
		record0.put(VariableNames.INDUSTRY_IN_DATABASE, "");
		record0.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "28");
		record0.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, "");
		record0.put(VariableNames.NAME, "Sony");

		Map<String, String> record1 = new HashMap<String, String>();
		record1.put(VariableNames.ID_IN_SALESFORCE, "1");
		record1.put(VariableNames.ID_IN_DATABASE, "1");
		record1.put(VariableNames.INDUSTRY_IN_SALESFORCE, "Pharmaceutic");
		record1.put(VariableNames.INDUSTRY_IN_DATABASE, "Experimental");
		record1.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "22");
		record1.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, "500");
		record1.put(VariableNames.NAME, "Generica");

		Map<String, String> record2 = new HashMap<String, String>();
		record2.put(VariableNames.ID_IN_SALESFORCE, "");
		record2.put(VariableNames.ID_IN_DATABASE, "2");
		record2.put(VariableNames.INDUSTRY_IN_SALESFORCE, "");
		record2.put(VariableNames.INDUSTRY_IN_DATABASE, "Energetic");
		record2.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "");
		record2.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, "4160");
		record2.put(VariableNames.NAME, "Global Voltage");

		List<Map<String, String>> expectedList = new ArrayList<Map<String, String>>();
		expectedList.add(record0);
		expectedList.add(record1);
		expectedList.add(record2);

		return expectedList;
	}

	static Map<String, String> createEmptyMergedRecord(Integer secuense) {
		Map<String, String> account = new HashMap<String, String>();
		account.put("Name", "SomeName_" + secuense);
		account.put("IDInA", "");
		account.put("IndustryInA", "");
		account.put("NumberOfEmployeesInA", "");
		account.put("IDInB", "");
		account.put("IndustryInB", "");
		account.put("NumberOfEmployeesInB", "");
		return account;
	}

	static List<Map<String, String>> createAccountLists(String orgId,
			int start, int end) {
		List<Map<String, String>> accountList = new ArrayList<Map<String, String>>();
		for (int i = start; i <= end; i++) {
			accountList.add(createAccount(orgId, i));
		}
		return accountList;
	}

	static Map<String, String> createAccount(String orgId, int sequence) {
		Map<String, String> account = new HashMap<String, String>();

		account.put("Id", new Integer(sequence).toString());
		account.put("Name", "SomeName_" + sequence);
		account.put("Industry", "Goverment");
		account.put("NumberOfEmployees", "550.0");

		return account;
	}
}