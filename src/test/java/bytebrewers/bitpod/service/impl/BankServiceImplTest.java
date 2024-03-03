package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.repository.BankRepository;
import bytebrewers.bitpod.utils.dto.request.bank.BankDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
@Slf4j
class BankServiceImplTest {
    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        Bank bank = Bank.builder()
                .name("Bank of America")
                .address("1234 Main St")
                .build();

        when(bankRepository.findById(1)).thenReturn(Optional.of(bank));

    }

    @Test
    void create() {
        BankDTO bank = BankDTO.builder()
                .name("Bank of America")
                .address("1234 Main St")
                .build();

        when(bankRepository.save(Mockito.any(Bank.class))).thenReturn(bank.toEntity());
        Bank savedBank = bankService.create(bank);

        assertThat(savedBank).isNotNull();
    }

    @Test
    void getById() {
        Bank bank = Bank.builder()
                .name("Bank of America")
                .address("1234 Main St")
                .build();

        when(bankRepository.findById(1)).thenReturn(Optional.of(bank));

        Bank savedBank = bankService.getById(1);
        assertThat(savedBank).isNotNull();
    }

    @Test
    void update() {
        Bank bank = Bank.builder()
                .id(1)
                .name("Bank of America")
                .address("1234 Main St")
                .build();

        when(bankRepository.findById(1)).thenReturn(Optional.of(bank));
        BankDTO bankDTO = BankDTO.builder()
                .name("Bank of Indonesia")
                .address("Jakarta")
                .build();

        bankService.update(bank.getId(), bankDTO);

        Bank banks = bankRepository.findById(1).get();

        assertThat(banks.getName()).isEqualTo(bankDTO.getName());
        assertThat(banks.getAddress()).isEqualTo(bankDTO.getAddress());
        assertThat(banks.getId()).isEqualTo(bank.getId());
    }

    @Test
    void delete() {
        Bank bank = Bank.builder()
                .id(1)
                .name("Bank of America")
                .address("1234 Main St")
                .build();
        when(bankRepository.findById(1)).thenReturn(Optional.of(bank));

        doNothing().when(bankRepository).deleteById(bank.getId());

        bankService.delete(bank.getId());

        verify(bankRepository, times(1)).deleteById(bank.getId());

        Bank deletedBank = bankRepository.findById(bank.getId()).orElse(null);

        assertThat(deletedBank).isNull();
    }

}