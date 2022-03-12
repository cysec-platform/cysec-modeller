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
public class ExternalControlDTO {

    private long id;
    private String key;
    private String name;
    private String details;
    private ExternalSourceDTO source;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer dependentsCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ControlDTO> dependents;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
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

    public ExternalSourceDTO getSource() {
        return source;
    }

    public void setSource(final ExternalSourceDTO source) {
        this.source = source;
    }

    public Integer getDependentsCount() {
        return dependentsCount;
    }

    public void setDependentsCount(final Integer dependentsCount) {
        this.dependentsCount = dependentsCount;
    }

    public List<ControlDTO> getDependents() {
        return dependents;
    }

    public void setDependents(final List<ControlDTO> dependents) {
        this.dependents = dependents;
    }
}
