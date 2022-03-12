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

import eu.smesec.cysec.modeller.persistence.ExternalControl;
import eu.smesec.cysec.modeller.persistence.ExternalControlRepository;
import eu.smesec.cysec.modeller.persistence.ExternalSource;
import eu.smesec.cysec.modeller.persistence.ExternalSourceRepository;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matthias Luppi
 */
@Component
public class ExternalDataImporter {

    private static final Logger logger = LoggerFactory.getLogger(ExternalDataImporter.class);

    private final ExternalSourceRepository externalSourceRepository;
    private final ExternalControlRepository externalControlRepository;

    @Autowired
    public ExternalDataImporter(final ExternalSourceRepository externalSourceRepository, final ExternalControlRepository externalControlRepository) {
        this.externalSourceRepository = externalSourceRepository;
        this.externalControlRepository = externalControlRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void importData(final File file) throws Exception {
        final HashMap<String, Integer> columnIndexToFieldName = new HashMap<>();

        logger.info("Starting import of external data from '{}'", file);
        try (FileInputStream excelFile = new FileInputStream(file)) {
            final Workbook workbook = new XSSFWorkbook(excelFile);
            final Sheet sheet = workbook.getSheet("ExternalSources");
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

            final List<ExternalSource> externalSources = externalSourceRepository.findAll();

            // get data
            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final ExternalControl control = new ExternalControl();
                control.setKey(ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("id"))));
                control.setName(ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("name"))));
                control.setDetails(ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("details"))));

                final String sourceKey = ImporterHelper.getValue(row.getCell(columnIndexToFieldName.get("source")));
                final ExternalSource externalSource = externalSources.stream()
                        .filter(s -> s.getKey().equals(sourceKey))
                        .findFirst()
                        .orElseGet(() -> {
                            ExternalSource newExternalSource = new ExternalSource();
                            newExternalSource.setKey(sourceKey);
                            newExternalSource.setName(sourceKey);
                            newExternalSource = externalSourceRepository.save(newExternalSource);
                            externalSources.add(newExternalSource);
                            return newExternalSource;
                        });
                control.setSource(externalSource);

                externalControlRepository.save(control);
            }
            logger.info("Import complete");
        } catch (Exception e) {
            logger.error("Error while importing");
            throw e;
        }
    }

}
