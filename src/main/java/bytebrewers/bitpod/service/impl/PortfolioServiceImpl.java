package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.PortfolioRepository;
import bytebrewers.bitpod.service.PortfolioService;
import bytebrewers.bitpod.utils.dto.request.portfolio.PortfolioDTO;
import bytebrewers.bitpod.utils.helper.EntityUpdater;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    @Override
    public Page<Portfolio> getAll(Pageable pageable, PortfolioDTO portfolioDTO) {
        Specification<Portfolio> specification = GeneralSpecification.getSpecification(portfolioDTO);
        return portfolioRepository.findAll(specification, pageable);
    }

    @Override
    public Portfolio create(PortfolioDTO portfolioDTO, User cred) {
        return portfolioRepository.save(portfolioDTO.toEntity(cred));
    }

    @Override
    public Portfolio getById(String id) {
        return Auditable.searchById(portfolioRepository.findById(id), "Portfolio not found");
    }

    @Override
    public Portfolio update(String id, PortfolioDTO portfolioDTO, User cred) {
        Portfolio existingPortfolio =  Auditable.searchById(portfolioRepository.findById(id), "Portfolio not found");
        EntityUpdater.updateEntity(existingPortfolio, portfolioDTO.toEntity(cred));
        return portfolioRepository.save(existingPortfolio);
    }

    @Override
    public void delete(String id) {
        Auditable.searchById(portfolioRepository.findById(id), "Portfolio not found");
        portfolioRepository.deleteById(id);
    }

    @Override
    public Portfolio getByUser(User user) {
        return portfolioRepository.findByUser(user);
    }
}
