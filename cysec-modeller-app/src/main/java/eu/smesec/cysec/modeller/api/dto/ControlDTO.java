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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Matthias Luppi
 */
public class ControlDTO {

    private long id;
    private String name;
    private String details;
    private CategoryDTO category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer dependenciesCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer dependentsCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sourcesCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ControlDTO> dependencies;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ControlDTO> dependents;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ExternalControlDTO> sources;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(final CategoryDTO category) {
        this.category = category;
    }

    public Integer getDependenciesCount() {
        return dependenciesCount;
    }

    public void setDependenciesCount(final Integer dependenciesCount) {
        this.dependenciesCount = dependenciesCount;
    }

    public Integer getDependentsCount() {
        return dependentsCount;
    }

    public void setDependentsCount(final Integer dependentsCount) {
        this.dependentsCount = dependentsCount;
    }

    public Integer getSourcesCount() {
        return sourcesCount;
    }

    public void setSourcesCount(final Integer sourcesCount) {
        this.sourcesCount = sourcesCount;
    }

    public List<ControlDTO> getDependencies() {
        return dependencies;
    }

    public void setDependencies(final List<ControlDTO> dependencies) {
        this.dependencies = dependencies;
    }

    public List<ControlDTO> getDependents() {
        return dependents;
    }

    public void setDependents(final List<ControlDTO> dependents) {
        this.dependents = dependents;
    }

    public List<ExternalControlDTO> getSources() {
        return sources;
    }

    public void setSources(final List<ExternalControlDTO> sources) {
        this.sources = sources;
    }
}
