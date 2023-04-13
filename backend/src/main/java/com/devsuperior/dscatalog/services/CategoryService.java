package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
   * Find all paged.
   *
   * @param pageRequest the page request
   * @return the page
   */
  @Transactional(readOnly = true)
  public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
    Page<Category> list = repository.findAll(pageRequest);
    // using page, we can delete .stream and .collect, because Page are
    // java 8 and already have these methods.
    // return: list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    // return: list.map(CategoryDTO::new).collect(Collectors.toList());
    return list.map(CategoryDTO::new); // shortcut return xD

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

  /**
   * Update category dto.
   *
   * @param id  the id
   * @param dto the dto
   * @return the category dto
   */
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

  /**
   * Delete.
   *
   * @param id the id
   */
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
