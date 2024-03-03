package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.repository.BankRepository;
import bytebrewers.bitpod.service.impl.BankServiceImpl;
import bytebrewers.bitpod.utils.dto.request.bank.BankDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BankServiceTest {
    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    @Test
    void getAll() {
    }

    @Test
    void create() {
        Bank bank = new Bank();

        BankDTO bankDTO = BankDTO.builder()
                .name("Bank of America")
                .address("New York")
                .build();

        when(bankRepository.save(Mockito.any(Bank.class))).thenReturn(bank);

        Bank savedBank = bankService.create(bankDTO);

        assertNotNull(savedBank);
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}