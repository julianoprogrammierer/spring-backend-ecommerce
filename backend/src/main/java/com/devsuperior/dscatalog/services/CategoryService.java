package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;


/**
 * The type Category service.
 */
@Service
public class CategoryService {
  @Autowired
  private CategoryRepository repository;

  /**
   * Find all list.
   *
   * @return the list
   */
  @Transactional(readOnly = true)
  public List<CategoryDTO> findAll() {
    List<Category> list = repository.findAll();
    // Uses a  lambda expression.
    //return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

    // Another option: use the method reference.
    return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
  }

  /**
   * Find by id category dto.
   *
   * @param id the id
   * @return the category dto
   */
  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    Optional<Category> obj = repository.findById(id);
    Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
    return new CategoryDTO(entity);
  }

  /**
   * Insert category dto.
   *
   * @param dto the dto
   * @return the category dto
   */
  @Transactional
  public CategoryDTO insert(CategoryDTO dto) {
    Category entity = new Category();
    entity.setName(dto.getName());
    entity = repository.save(entity);
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO dto) {
    try {
      Category entity = repository.getOne(id);
      entity.setName(dto.getName());
      entity = repository.save(entity);
      return new CategoryDTO(entity);

    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + id);

    }
  }

  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Id not found: " + id);

    } catch (DataIntegrityViolationException e) {
      throw new DataBaseException("Integrity violation");

    }
  }
}
