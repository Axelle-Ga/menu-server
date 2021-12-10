package com.cicdlectures.menuserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cicdlectures.menuserver.dto.DishDto;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;
import com.cicdlectures.menuserver.repository.MenuRepository;
import com.cicdlectures.menuserver.repository.DishRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateMenuServiceTests {

    private MenuRepository menuRepository;

    private DishRepository dishRepository;

    private CreateMenuService subject;

    @BeforeEach
    public void init() {
      menuRepository = mock(MenuRepository.class);
      dishRepository = mock(DishRepository.class);
      subject = new CreateMenuService(this.menuRepository, this.dishRepository);
    }

    @Test
    @DisplayName("deduplication of dishes")
    public void deduplicationTest(){

        // Create a menu dto
        MenuDto menu = new MenuDto(
            Long.valueOf(1),
            "Christmas menu",
            new HashSet<>(
                Arrays.asList(
                new DishDto(Long.valueOf(1), "Turkey"),
                new DishDto(Long.valueOf(2), "Pecan Pie"),
                new DishDto(Long.valueOf(2), "Pecan Pie")
                ))
        );

        //Act
        subject.createMenu(menu);

        long gotDishesCount = dishRepository.count();


        assertEquals(2, gotDishesCount);
    }
    
}
