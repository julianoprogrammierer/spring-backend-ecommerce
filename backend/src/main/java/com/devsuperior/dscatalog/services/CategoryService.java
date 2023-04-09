package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not Found"));
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
}
