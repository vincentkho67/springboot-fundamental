package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.repository.BankRepository;
import bytebrewers.bitpod.service.impl.BankServiceImpl;
import bytebrewers.bitpod.utils.dto.request.bank.BankDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Slf4j
public class BankServiceTest {
    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        bank.setId(1);
        bank.setName("BCA");
        bank.setAddress("jakarta");
    }

    @DisplayName("JUnit test for save Bank")
    @Test
    public void givenBankObject_whenSave_thenReturnSavedBank() {
        //given
        BankDTO bankDTO = new BankDTO(bank.getName(), bank.getAddress());
        given(bankRepository.save(Mockito.any(Bank.class))).willReturn(bank);

        //when
        Bank saveBank = bankService.create(bankDTO);
        log.info("saved bank {}", saveBank.toString());

        //then
        assertNotNull(saveBank);
        assertEquals(saveBank.getName(), bank.getName());
        assertEquals(saveBank.getAddress(), bank.getAddress());
    }

    @DisplayName("JUnit test for get all Bank")
    @Test
    public void givenBankObject_whenFindAllBank_thenReturnBank() {
        //given
        List<Bank> bankList = new ArrayList<>();
        bankList.add(bank);
        bankList.add(bank);
        bankList.add(bank);

        Page<Bank> bankPage = new PageImpl<>(bankList);

        // Mocking repository method
        given(bankRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(bankPage);

        // Testing the service method
        Page<Bank> result = bankService.getAll(Pageable.unpaged(), new BankDTO());

        // Verifying that the repository method was called with the expected parameters
        verify(bankRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

        // Verifying that the result matches the mock data
        assertEquals(bankPage.getTotalElements(), result.getTotalElements());
        assertEquals(bankPage, result);
    }

    @DisplayName("JUnit test for get Bank by id ")
    @Test
    public void givenBankObject_whenFindById_thenReturnBank() {
        //given
        given(bankRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.of(bank));

        //when
        Bank bankServiceById = bankService.getById(bank.getId());
        log.info("this is bank service by id {}", bankServiceById);

        //then
        assertNotNull(bankServiceById);
        assertEquals(bankServiceById.getId(), bank.getId());
        assertEquals(bankServiceById.getName(), bank.getName());
        assertEquals(bankServiceById.getAddress(), bank.getAddress());
    }

    @DisplayName("JUnit test for update bank")
    @Test
    public void givenBankObject_whenUpdate_thenUpdateBank() {
        Integer bankId = 1;
        BankDTO bankDTO = new BankDTO("MANDIRI", "Bogor");

        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);

        // Act
        Bank result = bankService.update(bankId, bankDTO);

        // Assert
        verify(bankRepository, times(1)).findById(bankId);
        verify(bankRepository, times(1)).save(bank);

        assertEquals(result.getName(), bankDTO.getName());
        assertEquals(result.getAddress(), bankDTO.getAddress());
    }

}