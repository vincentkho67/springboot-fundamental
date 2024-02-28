package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.utils.dto.request.portfolio.PortfolioSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PortfolioService {
    Page<Portfolio> getAll(Pageable pageable, PortfolioSearchDTO portfolioSearchDTO);
    Portfolio create(Portfolio portfolio);
    Portfolio getById(String id);
    Portfolio update(String id, Portfolio portfolio);
    void delete(String id);
}
