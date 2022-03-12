/*-
 * #%L
 * CYSEC Modeller App
 * %%
 * Copyright (C) 2021 - 2022 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.smesec.cysec.modeller.api;

import eu.smesec.cysec.modeller.api.dto.ControlDTO;
import eu.smesec.cysec.modeller.persistence.Category;
import eu.smesec.cysec.modeller.api.dto.CategoryDTO;
import eu.smesec.cysec.modeller.api.dto.ExternalSourceDTO;
import eu.smesec.cysec.modeller.api.dto.Mapper;
import eu.smesec.cysec.modeller.persistence.CategoryRepository;
import eu.smesec.cysec.modeller.persistence.Control;
import eu.smesec.cysec.modeller.persistence.ControlRepository;
import eu.smesec.cysec.modeller.persistence.ExternalControl;
import eu.smesec.cysec.modeller.persistence.ExternalSource;
import eu.smesec.cysec.modeller.persistence.ExternalSourceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author Matthias Luppi
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ControlRepository controlRepository;
    private final CategoryRepository categoryRepository;
    private final ExternalSourceRepository externalSourceRepository;

    public ApiController(final ControlRepository controlRepository,
                         final CategoryRepository categoryRepository,
                         final ExternalSourceRepository externalSourceRepository) {
        this.controlRepository = controlRepository;
        this.categoryRepository = categoryRepository;
        this.externalSourceRepository = externalSourceRepository;
    }

    @GetMapping("/control")
    public List<ControlDTO> getControls() {
        return controlRepository.findAll().stream()
                .map(Mapper::shallowMap)
                .sorted(Comparator.comparing((ControlDTO c) -> c.getCategory().getName()).thenComparing(ControlDTO::getName))
                .collect(Collectors.toList());
    }

    @GetMapping("/external-source")
    public List<ExternalSourceDTO> getExternalSources(@RequestParam(name = "withControls", defaultValue = "false") final boolean withControls) {
        return externalSourceRepository.findAll().stream()
                .sorted(Comparator.comparing(ExternalSource::getName))
                .map(c -> withControls ? Mapper.map(c) : Mapper.shallowMap(c))
                .collect(Collectors.toList());
    }

    @GetMapping("/control/{id}")
    public ResponseEntity<ControlDTO> getControl(@PathVariable("id") long id) {
        return ResponseEntity.of(controlRepository.findById(id).map(Mapper::map));
    }

    @PostMapping("/control")
    public ResponseEntity<Long> addControl(@RequestBody ControlDTO dto) {
        final Optional<Category> category = categoryRepository.findById(dto.getCategory().getId());
        if (category.isPresent()) {
            Control control = new Control();
            control.setName(dto.getName());
            control.setDetails(dto.getDetails());
            control.setCategory(category.get());
            control = controlRepository.save(control);
            return ResponseEntity.ok(control.getId());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category")
    public List<CategoryDTO> getCategories(@RequestParam(name = "withControls", defaultValue = "false") final boolean withControls) {
        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::getName))
                .map(c -> withControls ? Mapper.map(c) : Mapper.shallowMap(c))
                .collect(Collectors.toList());
    }

    @GetMapping("/data/stats.csv")
    public void getStatsCsv(final HttpServletResponse response) throws IOException {
        final List<Category> categories = categoryRepository.findAll(Sort.by("name"));
        final List<String> externalSourcesKeys = categories.stream()
                .flatMap(c -> c.getControls().stream())
                .flatMap(c -> c.getSources().stream())
                .map(ExternalControl::getSource)
                .map(ExternalSource::getKey)
                .distinct()
                .collect(Collectors.toList());

        final StringJoiner header = new StringJoiner(";");
        header.add("Name");
        header.add("Controls");
        externalSourcesKeys.forEach(header::add);
        response.getWriter().println(header);

        for (final Category category : categories) {
            final StringJoiner line = new StringJoiner(";");
            line.add(category.getName());
            line.add(Integer.toString(category.getControls().size()));

            final List<ExternalControl> externalControls = category.getControls().stream()
                    .flatMap(c -> c.getSources().stream())
                    .distinct()
                    .collect(Collectors.toList());

            externalSourcesKeys.forEach(key -> {
                final long referencesCount = externalControls.stream()
                        .filter(ec -> key.equals(ec.getSource().getKey()))
                        .count();
                line.add(Long.toString(referencesCount));
            });

            response.getWriter().println(line);
        }
    }

}
