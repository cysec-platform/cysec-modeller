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

import eu.smesec.cysec.modeller.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @author Matthias Luppi
 */
@Component
public class ModelImportRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ModelImportRunner.class);

    private final ExternalDataImporter externalDataImporter;
    private final ModelDataImporter modelDataImporter;
    private final AppConfig appConfig;

    @Autowired
    public ModelImportRunner(final ExternalDataImporter externalDataImporter,
                             final ModelDataImporter modelDataImporter,
                             final AppConfig appConfig) {
        this.externalDataImporter = externalDataImporter;
        this.modelDataImporter = modelDataImporter;
        this.appConfig = appConfig;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void run(final String... args) throws Exception {
        logger.info("Configured model file is '{}'", appConfig.getModelFile());
        final File modelFile = new File(appConfig.getModelFile());
        externalDataImporter.importData(modelFile);
        modelDataImporter.importData(modelFile);
    }
}
