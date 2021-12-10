package com.cicdlectures.menuserver.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import com.cicdlectures.menuserver.dto.DishDto;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;
import com.cicdlectures.menuserver.repository.MenuRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

// src/test/java/com/cicdlectures/menuserver/controller/MenuControllerIT.java
// Lance l'application sur un port aléatoire.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Indique de relancer l'application à chaque test.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MenuControllerIT {

    @LocalServerPort
    private int port;

    private URL getMenusURL() throws Exception {
    return new URL("http://localhost:" + port + "/menus");
    }

    // Injecte automatiquement l'instance du menu repository
    @Autowired
    private MenuRepository menuRepository;

    // Injecte automatiquement l'instance du TestRestTemplate
    @Autowired
    private TestRestTemplate template;

    @Test
    @DisplayName("lists all known menus")
    public void listsAllMenus() throws Exception {

        // Défini une liste de menus avec un menus.
        Menu menu =
            new Menu(
            null,
            "Christmas menu",
            new HashSet<>(
                Arrays.asList(
                new Dish(null, "Turkey", null),
                new Dish(null, "Pecan Pie", null)
                )
            )
            );

        menu = menuRepository.save(menu);
        
        // Effectue une requête GET /menus
        ResponseEntity<MenuDto[]> response = this.template.getForEntity(getMenusURL().toString(), MenuDto[].class);

        //Parse le payload de la réponse sous forme d'array de MenuDto
        MenuDto[] gotMenus = response.getBody();
        HttpStatus statusCode = response.getStatusCode();

         // Défini une liste de menus avec un menus.
         MenuDto existingMenusDto =
            new MenuDto(
            Long.valueOf(1),
            "Christmas menu",
            new HashSet<>(
                Arrays.asList(
                new DishDto(Long.valueOf(1), "Turkey"),
                new DishDto(Long.valueOf(2), "Pecan Pie")
                )
            )
            );


        // Assert that correct status code is returned.
        assertEquals(statusCode /*actual value*/, HttpStatus.OK /*expected value*/, "Correct status code returned");
        assertEquals(gotMenus[0] /*actual value*/, existingMenusDto /*expected value*/, "Correct menu returned");
        

    }
}