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
package eu.smesec.cysec.modeller.api.dto;

import eu.smesec.cysec.modeller.persistence.Category;
import eu.smesec.cysec.modeller.persistence.Control;
import eu.smesec.cysec.modeller.persistence.ExternalControl;
import eu.smesec.cysec.modeller.persistence.ExternalSource;

import java.util.stream.Collectors;

/**
 * @author Matthias Luppi
 */
public class Mapper {

    public static CategoryDTO map(final Category category) {
        final CategoryDTO dto = shallowMap(category);
        dto.setControls(category.getControls().stream().map(Mapper::shallowMap).collect(Collectors.toList()));
        return dto;
    }

    public static CategoryDTO shallowMap(final Category category) {
        final CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setControlsCount(category.getControls().size());
        return dto;
    }

    public static ControlDTO map(final Control control) {
        final ControlDTO dto = Mapper.shallowMap(control);
        dto.setDependencies(control.getDependencies().stream().map(Mapper::shallowMap).collect(Collectors.toList()));
        dto.setDependents(control.getDependents().stream().map(Mapper::shallowMap).collect(Collectors.toList()));
        dto.setSources(control.getSources().stream().map(Mapper::map).collect(Collectors.toList()));
        return dto;
    }

    public static ControlDTO shallowMap(final Control control) {
        final ControlDTO dto = new ControlDTO();
        dto.setId(control.getId());
        dto.setName(control.getName());
        dto.setDetails(control.getDetails());
        dto.setCategory(Mapper.shallowMap(control.getCategory()));
        dto.setDependenciesCount(control.getDependencies().size());
        dto.setDependentsCount(control.getDependents().size());
        dto.setSourcesCount(control.getSources().size());
        return dto;
    }

    public static ExternalControlDTO map(final ExternalControl externalControl) {
        final ExternalControlDTO dto = new ExternalControlDTO();
        dto.setId(externalControl.getId());
        dto.setKey(externalControl.getKey());
        dto.setName(externalControl.getName());
        dto.setDetails(externalControl.getDetails());
        dto.setSource(Mapper.shallowMap(externalControl.getSource()));
        dto.setDependentsCount(externalControl.getDependents().size());
        dto.setDependents(externalControl.getDependents().stream().map(Mapper::shallowMap).collect(Collectors.toList()));
        return dto;
    }

    public static ExternalSourceDTO map(final ExternalSource externalSource) {
        final ExternalSourceDTO dto = shallowMap(externalSource);
        dto.setControls(externalSource.getControls().stream().map(Mapper::map).collect(Collectors.toList()));
        return dto;
    }

    public static ExternalSourceDTO shallowMap(final ExternalSource externalSource) {
        final ExternalSourceDTO dto = new ExternalSourceDTO();
        dto.setId(externalSource.getId());
        dto.setKey(externalSource.getKey());
        dto.setName(externalSource.getName());
        return dto;
    }

    private Mapper() {
    }
}
