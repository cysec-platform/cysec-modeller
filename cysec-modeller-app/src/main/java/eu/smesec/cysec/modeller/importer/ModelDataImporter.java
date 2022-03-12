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
package eu.smesec.cysec.modeller.importer;

import eu.smesec.cysec.modeller.persistence.Category;
import eu.smesec.cysec.modeller.persistence.CategoryRepository;
import eu.smesec.cysec.modeller.persistence.Control;
import eu.smesec.cysec.modeller.persistence.ControlRepository;
import eu.smesec.cysec.modeller.persistence.ExternalControl;
import eu.smesec.cysec.modeller.persistence.ExternalControlRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Matthias Luppi
 */
@Component
public class ModelDataImporter {

    private static final Logger logger = LoggerFactory.getLogger(ModelDataImporter.class);

    private final CategoryRepository categoryRepository;
    private final ControlRepository controlRepository;
    private final ExternalControlRepository externalControlRepository;

    @Autowired
    public ModelDataImporter(final CategoryRepository categoryRepository,
                             final ControlRepository controlRepository,
                             final ExternalControlRepository externalControlRepository) {
        this.categoryRepository = categoryRepository;
        this.controlRepository = controlRepository;
        this.externalControlRepository = externalControlRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void importData(final File file) throws Exception {
        final HashMap<String, Integer> columnIndexToFieldName = new HashMap<>();
        final HashMap<String, Set<String>> dependenyMap = new HashMap<>();

        logger.info("Starting import of model data from '{}'", file);
        try (FileInputStream excelFile = new FileInputStream(file)) {
            final Workbook workbook = new XSSFWorkbook(excelFile);
            final Sheet sheet = workbook.getSheet("Model");
            final Iterator<Row> rowIterator = sheet.iterator();

            // check if all headers are present and correct
            if (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final Iterator<Cell> cellIterator = row.cellIterator();
                int i = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    final String variableName = cell.getStringCellValue().toLowerCase();
                    columnIndexToFieldName.put(variableName, i++);
                }
            }

            final List<Category> categories = categoryRepository.findAll();
            final List<ExternalControl> externalControls = externalControlRepository.findAll();

            // get data
            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final Control control = new Control();
                control.setName(ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("control"))));
                control.setKey(ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("key"))));
                control.setDetails(ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("remarks"))));

                final String categoryName = ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("category")));
                final Category category = categories.stream()
                        .filter(s -> s.getName().equals(categoryName))
                        .findFirst()
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(categoryName);
                            newCategory = categoryRepository.save(newCategory);
                            categories.add(newCategory);
                            return newCategory;
                        });
                control.setCategory(category);

                addSources(columnIndexToFieldName, externalControls, row, control, "ISFAM");
                addSources(columnIndexToFieldName, externalControls, row, control, "MSA");
                addSources(columnIndexToFieldName, externalControls, row, control, "CIS");
                addSources(columnIndexToFieldName, externalControls, row, control, "QT");

                String dependenciesString = ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("dependencies")));
                if (dependenciesString != null) {
                    final Set<String> controlDependencies = Arrays.stream(dependenciesString.split("(,\\s*)"))
                            .filter(s -> !s.isBlank())
                            .collect(Collectors.toSet());
                    dependenyMap.put(control.getKey(), controlDependencies);
                }

                controlRepository.save(control);
            }

            // link dependencies
            dependenyMap.forEach((originKey, dependencies) -> {
                final Optional<Control> originControl = controlRepository.findControlByKeyEquals(originKey);
                if (originControl.isPresent()) {
                    dependencies.forEach(dependencyKey -> {
                        final Control dependencyControl = controlRepository.findControlByKeyEquals(dependencyKey).orElseThrow();
                        originControl.get().getDependencies().add(dependencyControl);
                    });
                    controlRepository.save(originControl.get());
                }
            });
            logger.info("Import complete");
        } catch (Exception e) {
            logger.error("Error while importing");
            throw e;
        }
    }

    private void addSources(final HashMap<String, Integer> columnIndexToFieldName, final List<ExternalControl> externalControls, final Row row, final Control control, final String source) {
        String keyString = ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get(source.toLowerCase())));
        if (keyString != null) {
            final String[] keys = keyString.split("(,\\s*)");
            for (String key : keys) {
                if (!key.isBlank()) {
                    final Optional<ExternalControl> externalControl = externalControls.stream()
                            .filter(c -> c.getKey().equals(key) && c.getSource().getKey().equals(source))
                            .findFirst();
                    if (externalControl.isPresent()) {
                        control.getSources().add(externalControl.get());
                    } else {
                        throw new IllegalStateException("Could not find external control '" + key + "' in '" + source + "' (referenced by '" + control.getName() + "')");
                    }
                }
            }
        }
    }

}
