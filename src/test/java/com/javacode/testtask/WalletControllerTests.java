package com.javacode.testtask;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.testtask.controller.WalletController;
import com.javacode.testtask.dto.WalletRequestDto;
import com.javacode.testtask.exceptions.InsufficientFundsException;
import com.javacode.testtask.exceptions.WalletNotFoundException;
import com.javacode.testtask.service.WalletService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = WalletController.class)
public class WalletControllerTests {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private @NotNull UUID walletId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        BDDMockito.given(this.walletService.getBalance(ArgumentMatchers.any())).willReturn(1000.0);
    }

    @Test
    @Order(0)
    void depositToWallet() throws Exception {
        var amount = 1000.0;
        var request = new WalletRequestDto(this.walletId, WalletRequestDto.Operation.DEPOSIT, amount);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/wallet")
                                .content(this.objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(1)
    void withdrawFromWallet() throws Exception {
        var amount = 500.0;
        var request = new WalletRequestDto(this.walletId, WalletRequestDto.Operation.WITHDRAW, amount);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/wallet")
                                .content(this.objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(2)
    void getBalanceOfExisitingWallet() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/" + this.walletId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(1000.0)));
    }

    @Test
    @Order(3)
    void getBalanceOfNonExisitingWallet() throws Exception {
        Mockito.when(this.walletService.getBalance(walletId)).thenThrow(new WalletNotFoundException("Wallet not found", this.walletId));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/" + walletId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}