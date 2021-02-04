package com.in4mo.task;

import com.in4mo.task.service.RegistryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EnableConfigurationProperties
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class TaskApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private RegistryService registryService;

	/**
	 * Balance tests
	 */
	@Test
	@Order(1)
	public void getInitialCumulativeBalanceTest() throws Exception {
		mockMvc.perform(get("/balance/cumulative"))
				.andExpect(status().isOk())
				.andExpect(content().string("6000.0"));
	}

	@Test
	@Order(6)
	public void getFinalCumulativeBalanceTest() throws Exception {
		mockMvc.perform(get("/balance/cumulative"))
				.andExpect(status().isOk())
				.andExpect(content().string("8500.0"));
	}


	@Test
	@Order(7)
	public void getFinalAllBalancesTest() throws Exception {
		mockMvc.perform(get("/balance/all"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("['Wallet']", Matchers.equalTo(1000.0)))
				.andExpect(jsonPath("['Savings']", Matchers.equalTo(5500.0)))
				.andExpect(jsonPath("['Insurance policy']", Matchers.equalTo(500.0)))
				.andExpect(jsonPath("['Food expenses']", Matchers.equalTo(1500.0)));
	}

	/**
	 * Registry tests
	 */

	@Test
	public void getRegistryRootTest() throws Exception {
		mockMvc.perform(get("/registry"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getRegistryDetailsTest() throws Exception {
		mockMvc.perform(get("/registry/4/details"))
				.andExpect(status().isOk())
				.andExpect(content().string("Registry(id=4, name=Food expenses, balance=1500.0, customerId=1)"));
	}

	@Test
	public void getRegistryBalanceTest() throws Exception {
		mockMvc.perform(get("/registry/4/balance"))
				.andExpect(status().isOk())
				.andExpect(content().string("1500.0"));
	}

	@Test
	@Order(2)
	public void doRechargeTest() throws Exception {
		double expectedNewBalance = 3500.0;
		mockMvc.perform(put("/registry/1/recharge?amount=2500.0"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/registry/1/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalance)));
	}

	/**
	 * Transfer tests
	 */

	@Test
	@Order(3)
	public void transferFromWalletToFoodExpensesTest() throws Exception {
		double expectedNewBalanceRegistry1 = 2000.0;
		double expectedNewBalanceRegistry2 = 1500.0;

		mockMvc.perform(post("/transfer/from/1/to/4?amount=1500.0"))
				.andExpect(status().isOk())
				.andExpect(content().string("Transferred: 1500.0 from registry of id: 1 to registry of id: 4"));

		mockMvc.perform(get("/registry/1/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalanceRegistry1)));
		mockMvc.perform(get("/registry/4/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalanceRegistry2)));

		mockMvc.perform(get("/transfers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].source", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].destination", Matchers.equalTo(4)))
				.andExpect(jsonPath("$[0].amount", Matchers.equalTo(1500.0)));
	}

	@Test
	@Order(4)
	public void transferFromSavingsToInsurancePolicyTest() throws Exception {
		double expectedNewBalanceRegistrySavings = 4500.0;
		double expectedNewBalanceRegistryInsurancePolicy = 500.0;

		mockMvc.perform(post("/transfer/from/2/to/3?amount=500.0"))
				.andExpect(status().isOk())
				.andExpect(content().string("Transferred: 500.0 from registry of id: 2 to registry of id: 3"));

		mockMvc.perform(get("/registry/2/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalanceRegistrySavings)));
		mockMvc.perform(get("/registry/3/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalanceRegistryInsurancePolicy)));

		mockMvc.perform(get("/transfers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[1].source", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[1].destination", Matchers.equalTo(3)))
				.andExpect(jsonPath("$[1].amount", Matchers.equalTo(500.0)));
	}

	@Test
	@Order(5)
	public void transferFromWalletToSavingsTest() throws Exception {
		double expectedNewBalanceRegistryWallet = 1000.0;
		double expectedNewBalanceRegistrySavings = 5500.0;

		mockMvc.perform(post("/transfer/from/1/to/2?amount=1000.0"))
				.andExpect(status().isOk())
				.andExpect(content().string("Transferred: 1000.0 from registry of id: 1 to registry of id: 2"));

		mockMvc.perform(get("/registry/1/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalanceRegistryWallet)));
		mockMvc.perform(get("/registry/2/balance"))
				.andExpect(content().string(Double.toString(expectedNewBalanceRegistrySavings)));

		mockMvc.perform(get("/transfers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$[2].id", Matchers.equalTo(3)))
				.andExpect(jsonPath("$[2].source", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[2].destination", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[2].amount", Matchers.equalTo(1000.0)));
	}

}
